import pytest

import six

import utils

import verta.dataset


pytest.skip("unstable back end support", allow_module_level=True)


class TestPath:
    def test_dirpath(self):
        dataset = verta.dataset.Path("modelapi_hypothesis/")
        assert len(dataset._msg.path.components) > 1

        for file_metadata in dataset._msg.path.components:
            assert file_metadata.path != ""
            assert file_metadata.size != 0
            assert file_metadata.last_modified_at_source != 0
            assert file_metadata.md5 != ""

    def test_filepath(self):
        dataset = verta.dataset.Path("modelapi_hypothesis/api_generator.py")

        assert len(dataset._msg.path.components) == 1

        file_metadata = dataset._msg.path.components[0]
        assert file_metadata.path != ""
        assert file_metadata.size != 0
        assert file_metadata.last_modified_at_source != 0
        assert file_metadata.md5 != ""

    def test_multiple_filepaths(self):
        dataset = verta.dataset.Path([
            "modelapi_hypothesis/api_generator.py",
            "modelapi_hypothesis/test_modelapi.py",
        ])
        assert len(dataset._msg.path.components) == 2

        for file_metadata in dataset._msg.path.components:
            assert file_metadata.path != ""
            assert file_metadata.size != 0
            assert file_metadata.last_modified_at_source != 0
            assert file_metadata.md5 != ""

    def test_no_duplicates(self):
        multiple_dataset = verta.dataset.Path([
            "modelapi_hypothesis/",
            "modelapi_hypothesis/api_generator.py",
        ])
        dir_dataset = verta.dataset.Path("modelapi_hypothesis/")
        assert len(multiple_dataset._msg.path.components) == len(dir_dataset._msg.path.components)
