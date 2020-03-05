package ai.verta.modeldb.versioning.blob;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import ai.verta.modeldb.versioning.BlobDAORdbImpl;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.FileHasher;
import ai.verta.modeldb.versioning.PathDatasetBlob;
import ai.verta.modeldb.versioning.S3DatasetBlob;
import ai.verta.modeldb.versioning.TreeElem;
import io.grpc.Status.Code;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Session;

/**
 * contains proto object and saves it to a tree and the database
 */
public abstract class BlobContainer {

  private final BlobExpanded blobExpanded;

  public BlobContainer(BlobExpanded blobExpanded) {
    this.blobExpanded = blobExpanded;
  }

  public static BlobContainer create(BlobExpanded blobExpanded) throws ModelDBException {
    switch (blobExpanded.getBlob().getContentCase()) {
      case DATASET:
        return new DatasetContainer(blobExpanded);
      case ENVIRONMENT:
        return new EnvironmentContainer(blobExpanded);
      case CONTENT_NOT_SET:
      default:
        break;
    }
    throw new ModelDBException("Unknown blob type", Code.INVALID_ARGUMENT);
  }

  public abstract void validate() throws ModelDBException;

  public List<String> getLocationList() {
    List<String> result = new LinkedList<>();
    //empty dir represents root folder
    result.add("");
    result.addAll(blobExpanded.getLocationList());
    return result;
  }

  public abstract void process(Session session, TreeElem rootTree, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException;
}
