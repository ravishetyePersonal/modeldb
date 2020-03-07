# -*- coding: utf-8 -*-

from __future__ import print_function

from .._protos.public.modeldb.versioning import Config_pb2 as _ConfigService

from .._repository import blob


class _Configuration(blob.Blob):
    def __init__(self):
        """
        Base class for configuration versioning. Not for human consumption.

        """
        super(_Configuration, self).__init__()

        self._msg = _ConfigService.ConfigBlob()
