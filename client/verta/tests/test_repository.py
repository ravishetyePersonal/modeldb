import pytest

import six

import utils

from verta._repository.commit import Commit
import verta.dataset


pytest.skip("unstable back end support", allow_module_level=True)


class TestRepository:
    def test_get_by_name(self, client, repository):
        retrieved_repo = client.get_or_create_repository(repository.name)
        assert repository.id == retrieved_repo.id

    def test_get_by_id(self, client, repository):
        retrieved_repo = client.get_or_create_repository(id=repository.id)
        assert repository.id == retrieved_repo.id


class TestCommit:
    def test_add_get_rm(self, commit):
        pytest.importorskip("boto3")

        dataset1 = verta.dataset.S3("s3://verta-starter/census-test.csv")
        dataset2 = verta.dataset.S3("s3://verta-starter/census-train.csv")
        path1 = "path/to/bananas"
        path2 = "path/to/still-bananas"

        commit.update(path1, dataset1)
        commit.update(path2, dataset2)
        assert commit.get(path1) == dataset1
        assert commit.get(path2) == dataset2

        commit.remove(path1)
        with pytest.raises(LookupError):
            commit.remove(path1)

        commit.remove(path2)
        assert not commit._blobs

    def test_save(self, commit):
        pytest.importorskip("boto3")

        dataset = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path = "path/to/bananas"

        commit.update(path, dataset)

        commit.save()

        assert commit.id

    def test_walk(self, commit):
        pytest.importorskip("boto3")

        commit.update("file-1", verta.dataset.S3("s3://verta-starter/census-test.csv"))
        commit.update("a/file-2", verta.dataset.S3("s3://verta-starter/census-train.csv"))
        commit.update("a/file-3", verta.dataset.S3("s3://verta-starter/imdb_master.csv"))
        commit.update("a/b/file-4", verta.dataset.S3("s3://verta-starter/reviews.ann"))
        commit.update("a/c/file-5", verta.dataset.S3("s3://verta-starter/spam.csv"))
        commit.save()

        # alphabetical order
        walk = commit.walk()
        assert ("", ["a"], ["file-1"]) == next(walk)
        assert ("a", ["b", "c"], ["file-2", "file-3"]) == next(walk)
        assert ("a/b", [], ["file-4"]) == next(walk)
        assert ("a/c", [], ["file-5"]) == next(walk)

        # mutable `folder_names`
        walk = commit.walk()
        next(walk)  # root
        del next(walk)[1][1]  # remove a/c from a's `folder_names`
        next(walk)  # a/b
        with pytest.raises(StopIteration):
            next(walk)  # a/c removed
