package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;
import java.security.NoSuchAlgorithmException;

interface CommitDAO {
  Response setCommit(Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException, NoSuchAlgorithmException;

  ListCommitsRequest.Response listCommits(
      ListCommitsRequest request, RepositoryFunction getRepository) throws ModelDBException;

  Commit getCommit(String commitHash, RepositoryFunction getRepository) throws ModelDBException;

  //  GetCommitBlobRequest.Response getCommitBlob(GetCommitBlobRequest request);
  //
  //  GetCommitFolderRequest.Response getCommitFolder(
  //      GetCommitFolderRequest request, ProtocolStringList split) throws ModelDBException;
}
