# -*- coding: utf-8 -*-

from __future__ import print_function

import collections

from ..external import six

from .._protos.public.modeldb.versioning import VersioningService_pb2 as _VersioningService

from .._internal_utils import _utils
from .. import dataset
from .. import environment
from . import blob as blob_module


class Commit(object):
    def __init__(self, conn, repo_id, parent_ids=None, id_=None):
        self._conn = conn

        self._repo_id = repo_id
        self._parent_ids = list(collections.OrderedDict.fromkeys(parent_ids or []))  # remove duplicates while maintaining order

        self.id = id_

        self._blobs = dict()

        # until Commits can be created from blob diffs, load in blobs
        if self.id is not None:
            self._update_blobs_from_commit(self.id)
        else:
            for parent_id in self._parent_ids:
                # parents will be read in first-to-last, possibly overwriting previous blobs
                self._update_blobs_from_commit(parent_id)

    def __repr__(self):
        if self.id is None:
            header = "unsaved Commit containing:"
        else:
            header = "Commit {} containing:".format(self.id)
        contents = '\n'.join((
            "{} ({})".format(path, blob.__class__.__name__)
            for path, blob
            in sorted(six.viewitems(self._blobs))
        ))
        if not contents:
            contents = "<no contents>"
        return '\n'.join((header, contents))

    @classmethod
    def _from_id(cls, conn, repo_id, id_):
        endpoint = "{}://{}/api/v1/modeldb/versioning/repositories/{}/commits/{}".format(
            conn.scheme,
            conn.socket,
            repo_id,
            id_,
        )
        response = _utils.make_request("GET", endpoint, conn)
        _utils.raise_for_http_error(response)

        response_msg = _utils.json_to_proto(response.json(),
                                            _VersioningService.GetCommitRequest.Response)
        commit_msg = response_msg.commit
        return cls(conn, repo_id, commit_msg.parent_shas, commit_msg.commit_sha)

    @staticmethod
    def _raise_lookup_error(path):
        e = LookupError("Commit does not contain path \"{}\"".format(path))
        six.raise_from(e, None)

    def _update_blobs_from_commit(self, id_):
        """Fetches commit `id_`'s blobs and stores them as objects in `self._blobs`."""
        endpoint = "{}://{}/api/v1/modeldb/versioning/repositories/{}/commits/{}/blobs".format(
            self._conn.scheme,
            self._conn.socket,
            self._repo_id,
            id_,
        )
        response = _utils.make_request("GET", endpoint, self._conn)
        _utils.raise_for_http_error(response)

        response_msg = _utils.json_to_proto(response.json(),
                                            _VersioningService.ListCommitBlobsRequest.Response)
        self._blobs.update({
            '/'.join(blob_msg.location): blob_msg_to_object(blob_msg.blob)
            for blob_msg
            in response_msg.blobs
        })

    def _become_child(self):
        """
        This method is for when `self` had been saved and is then modified, meaning that
        this commit object has become a child of the commit that had been saved.

        """
        self._parent_ids = [self.id]
        self.id = None

    def _to_create_msg(self):
        msg = _VersioningService.CreateCommitRequest()
        msg.repository_id.repo_id = self._repo_id  # pylint: disable=no-member
        msg.commit.parent_shas.extend(self._parent_ids)  # pylint: disable=no-member

        for path, blob in six.viewitems(self._blobs):
            blob_msg = _VersioningService.BlobExpanded()
            blob_msg.location.extend(path_to_location(path))  # pylint: disable=no-member
            # TODO: move typecheck & CopyFrom to root blob base class
            if isinstance(blob, dataset._Dataset):
                blob_msg.blob.dataset.CopyFrom(blob._msg)  # pylint: disable=no-member
            elif isinstance(blob, environment._Environment):
                blob_msg.blob.environment.CopyFrom(blob._msg)  # pylint: disable=no-member
            else:
                raise RuntimeError("Commit contains an unexpected item {};"
                                   " please notify the Verta development team".format(type(blob)))
            msg.blobs.append(blob_msg)  # pylint: disable=no-member

        return msg

    def walk(self):
        """
        Generates folder names and blob names in this commit by walking through its folder tree.

        Similar to the Python standard library's ``os.walk()``, the yielded `folder_names` can be
        modified in-place to remove subfolders from upcoming iterations or alter the order in which
        they are to be visited.

        Note that, also similar to ``os.walk()``, `folder_names` and `blob_names` are simply the
        *names* of those entities, and *not* their full paths.

        Yields
        ------
        folder_path : str
            Path to current folder.
        folder_names : list of str
            Names of subfolders in `folder_path`.
        blob_names : list of str
            Names of blobs in `folder_path`.

        """
        if self.id is None:
            raise RuntimeError("Commit must be saved before it can be walked")

        endpoint = "{}://{}/api/v1/modeldb/versioning/repositories/{}/commits/{}/path".format(
            self._conn.scheme,
            self._conn.socket,
            self._repo_id,
            self.id,
        )

        locations = [()]
        while locations:
            location = locations.pop()

            msg = _VersioningService.GetCommitComponentRequest()
            msg.location.extend(location)  # pylint: disable=no-member
            data = _utils.proto_to_json(msg)
            response = _utils.make_request("GET", endpoint, self._conn, params=data)
            _utils.raise_for_http_error(response)

            response_msg = _utils.json_to_proto(response.json(), msg.Response)
            folder_msg = response_msg.folder

            folder_path = '/'.join(location)
            folder_names = list(sorted(element.element_name for element in folder_msg.sub_folders))
            blob_names = list(sorted(element.element_name for element in folder_msg.blobs))
            yield (folder_path, folder_names, blob_names)

            locations.extend(
                location + (folder_name,)
                for folder_name
                in reversed(folder_names)  # maintains order, because locations are popped from end
            )

    def update(self, path, blob):
        if not isinstance(blob, blob_module.Blob):
            raise TypeError("unsupported type {}".format(type(blob)))

        if self.id is not None:
            self._become_child()

        self._blobs[path] = blob

    def get(self, path):
        try:
            return self._blobs[path]
        except KeyError:
            self._raise_lookup_error(path)

    def remove(self, path):
        if self.id is not None:
            self._become_child()

        try:
            del self._blobs[path]
        except KeyError:
            self._raise_lookup_error(path)

    def save(self):
        msg = self._to_create_msg()
        data = _utils.proto_to_json(msg)
        endpoint = "{}://{}/api/v1/modeldb/versioning/repositories/{}/commits".format(
            self._conn.scheme,
            self._conn.socket,
            self._repo_id,
        )
        response = _utils.make_request("POST", endpoint, self._conn, json=data)
        _utils.raise_for_http_error(response)

        response_msg = _utils.json_to_proto(response.json(), msg.Response)
        self.id = response_msg.commit.commit_sha

    def tag(self, tag):
        if self.id is None:
            raise RuntimeError("Commit must be saved before it can be tagged")

        data = self.id
        endpoint = "{}://{}/api/v1/modeldb/versioning/repositories/{}/tags/{}".format(
            self._conn.scheme,
            self._conn.socket,
            self._repo_id,
            tag,
        )
        response = _utils.make_request("PUT", endpoint, self._conn, json=data)
        _utils.raise_for_http_error(response)


def blob_msg_to_object(blob_msg):
    # TODO: make this more concise
    content_type = blob_msg.WhichOneof('content')
    if content_type == 'dataset':
        dataset_type = blob_msg.dataset.WhichOneof('content')
        if dataset_type == 's3':
            obj = dataset.S3(paths=[])
        elif dataset_type == 'path':
            raise NotImplementedError
        else:
            raise NotImplementedError("found unexpected dataset type {};"
                                      " please notify the Verta development team".format(dataset_type))
    elif content_type == 'environment':
        environment_type = blob_msg.environment.WhichOneof('content')
        if environment_type == 'python':
            obj = environment.Python()
        elif environment_type == 'docker':
            raise NotImplementedError
        else:
            raise NotImplementedError("found unexpected environment type {};"
                                      " please notify the Verta development team".format(dataset_type))
    else:
        raise NotImplementedError("found unexpected content type {};"
                                  " please notify the Verta development team".format(content_type))

    obj._msg.CopyFrom(getattr(blob_msg, content_type))
    return obj


def path_to_location(path):
    """Messages take a `repeated string` of path components."""
    if path.startswith('/'):
        # `path` is already meant to be relative to repo root
        path = path[1:]

    return path.split('/')
