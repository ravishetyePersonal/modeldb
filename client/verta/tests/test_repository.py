import pytest

import six

import utils

from verta._repository.commit import Commit
import verta.dataset


class TestRepository:
    @pytest.mark.skip("endpoint having issues with get by name")
    def test_get_by_name(self, client, strs):
        name = strs[0]

        repo = client.get_or_create_repository(name)
        try:
            retrieved_repo = client.get_or_create_repository(name)
            assert repo.id == retrieved_repo.id
        finally:
            utils.delete_repository(repo.id, client._conn)

    def test_get_by_id(self, client, strs):
        name = strs[0]

        repo = client.get_or_create_repository(name)
        try:
            retrieved_repo = client.get_or_create_repository(id=repo.id)
            assert repo.id == retrieved_repo.id
        finally:
            utils.delete_repository(repo.id, client._conn)


class TestCommit:
    def test_add_get_rm(self):
        # pylint: disable=no-member
        pytest.importorskip("boto3")

        commit = Commit(None, None)  # dummy args

        dataset = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path1 = "path/to/bananas"
        path2 = "path/to/still-bananas"

        commit.update(path1, dataset)
        commit.update(path2, dataset)
        assert path1 in commit._blobs
        assert path2 in commit._blobs

        local_dataset_url = dataset._msg.components[0].path.path
        retrieved_dataset_url = commit.get(path1)._msg.components[0].path.path
        assert local_dataset_url == retrieved_dataset_url

        commit.remove(path1)
        with pytest.raises(LookupError):
            commit.remove(path1)

        commit.remove(path2)
        assert not commit._blobs

    def test_to_create_msg(self):
        # pylint: disable=no-member
        pytest.importorskip("boto3")

        commit = Commit(None, None)  # dummy args

        dataset = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path = "path/to/bananas"

        commit.update(path, dataset)

        msg = commit._to_create_msg()
        assert msg.blobs[0].path == path
        assert msg.blobs[0].blob.dataset.s3 == dataset._msg
