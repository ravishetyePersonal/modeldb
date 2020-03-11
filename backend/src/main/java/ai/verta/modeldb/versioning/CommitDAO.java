package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;
import ai.verta.modeldb.versioning.blob.container.BlobContainer;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.hibernate.Session;

interface CommitDAO {
  Response setCommit(
      String author, Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException, NoSuchAlgorithmException;

  ListCommitsRequest.Response listCommits(
      ListCommitsRequest request, RepositoryFunction getRepository) throws ModelDBException;

  Commit getCommit(String commitHash, RepositoryFunction getRepository) throws ModelDBException;

  CommitEntity getCommitEntity(
      Session session, String commitHash, RepositoryFunction getRepositoryFunction)
      throws ModelDBException;

  DeleteCommitRequest.Response deleteCommit(String commitHash, RepositoryFunction getRepository)
      throws ModelDBException;
}
