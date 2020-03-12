# -*- coding: utf-8 -*-

from __future__ import print_function


class Diff(object):
    def __init__(self, diffs):
        # TODO: order so that additions come after removal?
        self._diffs = diffs
