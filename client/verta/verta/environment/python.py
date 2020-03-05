# -*- coding: utf-8 -*-

from __future__ import print_function

import sys

from .._protos.public.modeldb.versioning import Environment_pb2 as _EnvironmentService

from . import _environment


class Python(_environment.Environment):
    def __init__(self, env_vars=None):
        """


        Parameters
        ----------
        env_vars : list of str, optional
            Names of environment variables to capture. If not provided, nothing will be captured.

        """
        super(Python, self).__init__(env_vars=env_vars)
        self._capture_py_ver()

    def _capture_py_ver(self):
        self._msg.python.version.major = sys.version_info.major
        self._msg.python.version.minor = sys.version_info.minor
        self._msg.python.version.patch = sys.version_info.micro
