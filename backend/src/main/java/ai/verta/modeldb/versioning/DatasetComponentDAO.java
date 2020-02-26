package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.hibernate.Session;

interface DatasetComponentDAO {

  String setBlobs(Session session, List<BlobExpanded> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException;

  GetCommitBlobRequest.Response getCommitBlob(
      RepositoryFunction repositoryFunction, String commitHash, String path)
      throws NoSuchAlgorithmException, ModelDBException;
}
