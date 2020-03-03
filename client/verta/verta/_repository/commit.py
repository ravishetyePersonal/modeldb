# -*- coding: utf-8 -*-

from __future__ import print_function

from ..external import six

from .._protos.public.modeldb.versioning import VersioningService_pb2 as _VersioningService

from .. import _utils
from .. import dataset


class Commit(object):
    def __init__(self, conn, repo_id, parent_ids=None, id_=None):
        self._conn = conn

        self._repo_id = repo_id
        self._parent_ids = parent_ids or []

        self.id = id_

        self._blobs = dict()

    def __repr__(self):
        header = "Commit containing:"
        contents = '\n'.join((
            "{} ({})".format(path, blob.__class__.__name__)
            for path, blob
            in sorted(six.viewitems(self._blobs))
        ))
        return '\n'.join((header, contents or "<no contents>"))

    @staticmethod
    def _raise_lookup_error(path):
        e = LookupError("Commit does not contain path \"{}\"".format(path))
        six.raise_from(e, None)

    def _to_create_msg(self):
        msg = _VersioningService.CreateCommitRequest()
        msg.repository_id.repo_id = self._repo_id  # pylint: disable=no-member
        msg.commit.parent_shas.extend(self._parent_ids)  # pylint: disable=no-member

        for path, blob in six.viewitems(self._blobs):
            blob_msg = _VersioningService.BlobExpanded()
            blob_msg.location.extend(path_to_location(path))  # pylint: disable=no-member
            if isinstance(blob, dataset.S3):  # TODO: move logic to root blob base class
                blob_msg.blob.dataset.s3.CopyFrom(blob._msg)  # pylint: disable=no-member
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
        if not isinstance(blob, dataset.S3):  # NOTE: needs to be the root blob base class
            raise TypeError("unsupported type {}".format(type(blob)))

        self._blobs[path] = blob

    def get(self, path):
        try:
            return self._blobs[path]
        except KeyError:
            # TODO: if `self` was saved commit, query back end for `path`
            self._raise_lookup_error(path)

    def remove(self, path):
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


def path_to_location(path):
    """Messages take a `repeated string` of path components."""
    if path.startswith('/'):
        # `path` is already meant to be relative to repo root
        path = path[1:]

    return path.split('/')
