import pytest

import os
import sys

import six

import utils

import verta.environment


pytest.skip("unstable back end support", allow_module_level=True)


class TestPython:
    def test_py_ver(self):
        env = verta.environment.Python()

        assert env._msg.python.version.major == sys.version_info.major
        assert env._msg.python.version.minor == sys.version_info.minor
        assert env._msg.python.version.patch == sys.version_info.micro

    def test_env_vars(self):
        env_vars = os.environ.keys()
        env = verta.environment.Python(env_vars=env_vars)

        assert env._msg.environment_variables

    def test_commit(self, repository):
        env = verta.environment.Python()

        commit1 = repository.new_commit()
        commit1.update('env', env)
        commit1.save()
        try:
            # get from internal blob dict
            assert commit1.get('env')

            commit2 = repository.new_commit(parents=[commit1])

            # get from back end
            assert commit2.get('env')
        finally:
            utils.delete_commit(repository.id, commit1.id, repository._conn)
