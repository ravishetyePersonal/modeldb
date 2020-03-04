package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.blob.BlobContainer;
import com.google.protobuf.ProtocolStringList;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.hibernate.Session;

interface DatasetComponentDAO {

  String setBlobs(Session session, List<BlobContainer> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException;

  GetCommitComponentRequest.Response getCommitComponent(
      RepositoryFunction repositoryFunction, String commitHash, ProtocolStringList locationList)
      throws NoSuchAlgorithmException, ModelDBException;
}
