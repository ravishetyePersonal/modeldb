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

    def test_get_commit_by_tag(self, repository):
        pytest.importorskip("boto3")

        dataset = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path = "path/to/bananas"

        commit = repository.new_commit()
        try:
            commit.update(path, dataset)
            commit.save()
            commit.tag("banana")

            assert commit.id == repository.get_commit(tag="banana").id
        finally:
            utils.delete_commit(repository.id, commit.id, repository._conn)

    def test_get_commit_by_id(self, repository):
        pytest.importorskip("boto3")

        dataset = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path = "path/to/bananas"

        commit = repository.new_commit()
        try:
            commit.update(path, dataset)
            commit.save()

            assert commit.id == repository.get_commit(id=commit.id).id
        finally:
            utils.delete_commit(repository.id, commit.id, repository._conn)

    def test_become_child(self, commit):
        pytest.importorskip("boto3")

        dataset1 = verta.dataset.S3("s3://verta-starter/census-test.csv")
        dataset2 = verta.dataset.S3("s3://verta-starter/census-train.csv")
        path1 = "path/to/bananas"
        path2 = "path/to/still-bananas"

        commit.update(path1, dataset1)
        commit.save()
        original_id = commit.id

        commit.update(path2, dataset2)
        assert commit.id is None
        assert original_id in commit._parent_ids
        assert commit.get(path1)

        commit.save()
        assert commit.id != original_id

    def test_set_parent(self, repository):
        pytest.importorskip("boto3")

        dataset1 = verta.dataset.S3("s3://verta-starter/census-test.csv")
        path1 = "path/to/bananas"

        commit1 = repository.new_commit()
        try:
            commit1.update(path1, dataset1)
            commit1.save()

            commit2 = repository.new_commit(parents=[commit1])
            assert commit1.id in commit2._parent_ids
            assert commit2.get(path1)
        finally:
            utils.delete_commit(repository.id, commit1.id, repository._conn)
