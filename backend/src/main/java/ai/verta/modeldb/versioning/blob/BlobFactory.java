package ai.verta.modeldb.versioning.blob;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import ai.verta.modeldb.versioning.Blob;
import io.grpc.Status.Code;
import org.hibernate.Session;

/**
 * constructs proto object from it's database implementation
 */
public abstract class BlobFactory {

  static final String S_3_DATASET_BLOB = "S3DatasetBlob";
  static final String PATH_DATASET_BLOB = "PathDatasetBlob";
  private static final String PYTHON_ENVIRONMENT_BLOB = "PythonEnvironmentBlob";
  private static final String DOCKER_ENVIRONMENT_BLOB = "DockerEnvironmentBlob";
  private final String elementType;
  private final String elementSha;

  BlobFactory(String elementType, String elementSha) {
    this.elementType = elementType;
    this.elementSha = elementSha;
  }

  public static BlobFactory create(InternalFolderElementEntity folderElementEntity)
      throws ModelDBException {
    switch (folderElementEntity.getElement_type()) {
      case S_3_DATASET_BLOB:
      case PATH_DATASET_BLOB:
        return new DatasetBlobFactory(folderElementEntity);
      case PYTHON_ENVIRONMENT_BLOB:
      case DOCKER_ENVIRONMENT_BLOB:
        return new EnvironmentBlobFactory(folderElementEntity);
      default:
        throw new ModelDBException(
            "Unknown blob type found " + folderElementEntity,
            Code.INTERNAL);
    }
  }


  public abstract Blob getBlob(Session session) throws ModelDBException;

  String getElementType() {
    return elementType;
  }

  String getElementSha() {
    return elementSha;
  }
}
