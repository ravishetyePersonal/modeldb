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
