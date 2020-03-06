import pytest

import os
import subprocess
import sys
import tempfile

import six

import utils

import verta.environment
from verta._internal_utils import _pip_requirements_utils


pytest.skip("unstable back end support", allow_module_level=True)


@pytest.fixture
def requirements_file():
    with tempfile.NamedTemporaryFile('w+') as tempf:
        # create constraints file from pip freeze
        pip_freeze = subprocess.check_output([sys.executable, '-m', 'pip', 'freeze'])
        pip_freeze = six.ensure_str(pip_freeze)
        tempf.write(pip_freeze)
        tempf.flush()  # flush object buffer
        os.fsync(tempf.fileno())  # flush OS buffer
        tempf.seek(0)

        yield tempf


@pytest.fixture
def requirements_file_without_versions():
    with tempfile.NamedTemporaryFile('w+') as tempf:
        # create constraints file from pip freeze
        pip_freeze = subprocess.check_output([sys.executable, '-m', 'pip', 'freeze'])
        pip_freeze = six.ensure_str(pip_freeze)
        stripped_pip_freeze = '\n'.join(
            line.split('==')[0]
            for line
            in pip_freeze.splitlines()
        )
        tempf.write(stripped_pip_freeze)
        tempf.flush()  # flush object buffer
        os.fsync(tempf.fileno())  # flush OS buffer
        tempf.seek(0)

        yield tempf


class TestUtils:
    def test_parse_pip_freeze(self):
        req_specs = _pip_requirements_utils.get_pip_freeze()
        parsed_req_specs = (
            (library, constraint, _pip_requirements_utils.parse_version(version))
            for library, constraint, version
            in map(_pip_requirements_utils.parse_req_spec, req_specs)
        )

        for library, constraint, parsed_version in parsed_req_specs:
            assert library != ""
            assert ' ' not in library

            assert constraint in _pip_requirements_utils.VER_SPEC_PATTERN.strip('()').split('|')

            assert parsed_version[0] >= 0  # major
            assert parsed_version[1] >= 0  # minor
            assert parsed_version[2] >= 0  # patch
            assert isinstance(parsed_version[3], six.string_types)  # suffix


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

    def test_reqs_from_env(self):
        env = verta.environment.Python()
        assert env._msg.python.requirements

    def test_reqs(self, requirements_file):
        reqs = verta.environment.Python.read_pip_file(requirements_file.name)
        env = verta.environment.Python(requirements=reqs)
        assert env._msg.python.requirements

    def test_reqs_without_versions(self, requirements_file_without_versions):
        reqs = verta.environment.Python.read_pip_file(requirements_file_without_versions.name)
        env = verta.environment.Python(requirements=reqs)
        assert env._msg.python.requirements

    def test_constraints_from_file(self, requirements_file):
        reqs = verta.environment.Python.read_pip_file(requirements_file.name)
        env = verta.environment.Python(constraints=reqs)
        assert env._msg.python.constraints

    def test_constraints_from_file_no_versions_error(self, requirements_file_without_versions):
        reqs = verta.environment.Python.read_pip_file(requirements_file_without_versions.name)
        with pytest.raises(ValueError):
            verta.environment.Python(constraints=reqs)
