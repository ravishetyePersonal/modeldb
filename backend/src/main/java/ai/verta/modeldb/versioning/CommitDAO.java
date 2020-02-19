package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;

interface CommitDAO {
  Response setCommit(Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException;
}
