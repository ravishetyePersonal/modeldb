package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;
import java.security.NoSuchAlgorithmException;

interface CommitDAO {
  Response setCommit(Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException, NoSuchAlgorithmException;

  Commit getCommit(String commitHash, RepositoryFunction getRepository) throws ModelDBException;
}
