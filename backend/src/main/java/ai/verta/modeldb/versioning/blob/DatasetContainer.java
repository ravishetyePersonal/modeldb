package ai.verta.modeldb.versioning.blob;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DatasetBlob;
import ai.verta.modeldb.versioning.FileHasher;
import ai.verta.modeldb.versioning.PathDatasetBlob;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob;
import ai.verta.modeldb.versioning.S3DatasetBlob;
import ai.verta.modeldb.versioning.S3DatasetComponentBlob;
import ai.verta.modeldb.versioning.TreeElem;
import io.grpc.Status.Code;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;

public class DatasetContainer extends BlobContainer {

  private final DatasetBlob dataset;

  public DatasetContainer(BlobExpanded blobExpanded) {
    super(blobExpanded);
    dataset = blobExpanded.getBlob().getDataset();
  }

  @Override
  public void validate() throws ModelDBException {
    switch (dataset.getContentCase()) {
      case S3:
        for (S3DatasetComponentBlob component : dataset.getS3().getComponentsList()) {
          if (!component.hasPath()) {
            throw new ModelDBException("Blob path should not be empty", Code.INVALID_ARGUMENT);
          }
        }
        break;
      case PATH:
        break;
      default:
        throw new ModelDBException("Blob unknown type", Code.INVALID_ARGUMENT);
    }
  }

  /**
   * @param rootTree : Each blob or folder need to be converted to a tree element. the process is
   *     bootstrapped with an empty tree for each BlobExpanded
   * @param fileHasher get sha of the blob or string
   * @throws NoSuchAlgorithmException for no hashing algorithm
   */
  @Override
  public void process(Session session, TreeElem rootTree, FileHasher fileHasher)
      throws NoSuchAlgorithmException {
    final List<String> locationList = getLocationList();
    String blobType = getBlobType();

    TreeElem treeChildDataset =
        rootTree.push(
            locationList,
            null,
            blobType,
            null); // need to ensure dataset is sorted
    switch (dataset.getContentCase()) {
      case S3:
        for (S3DatasetComponentBlob componentBlob : dataset.getS3().getComponentsList()) {
          final String sha256 = computeSHA(componentBlob);
          S3DatasetComponentBlobEntity s3DatasetComponentBlobEntity =
              new S3DatasetComponentBlobEntity(sha256, componentBlob);
          treeChildDataset.push(
              Arrays.asList(
                  locationList.get(locationList.size() - 1), componentBlob.getPath().getPath()),
              computeSHA(componentBlob.getPath()),
              componentBlob.getClass().getSimpleName(),
              s3DatasetComponentBlobEntity);
        }
        break;
      case PATH:
        for (PathDatasetComponentBlob componentBlob : dataset.getPath().getComponentsList()) {
          final String sha256 = computeSHA(componentBlob);
          PathDatasetComponentBlobEntity pathDatasetComponentBlobEntity =
              new PathDatasetComponentBlobEntity(sha256, componentBlob);
          treeChildDataset.push(
              Arrays.asList(locationList.get(locationList.size() - 1), componentBlob.getPath()),
              computeSHA(componentBlob),
              componentBlob.getClass().getSimpleName(),
              pathDatasetComponentBlobEntity);
        }
        break;
      default:
        break;
    }
  }

  private String getBlobType() {
    switch (dataset.getContentCase()) {
      case PATH:
        return PathDatasetBlob.class.getSimpleName();
      case S3:
      default:
        return S3DatasetBlob.class.getSimpleName();
    }
  }

  private String computeSHA(S3DatasetComponentBlob s3componentBlob)
      throws NoSuchAlgorithmException {
    StringBuilder sb = new StringBuilder();
    sb.append(":path:").append(computeSHA(s3componentBlob.getPath()));
    return FileHasher.getSha(sb.toString());
  }

  private String computeSHA(PathDatasetComponentBlob path) throws NoSuchAlgorithmException {
    StringBuilder sb = new StringBuilder();
    sb.append("path:")
        .append(path.getPath())
        .append(":size:")
        .append(path.getSize())
        .append(":last_modified:")
        .append(path.getLastModifiedAtSource())
        .append(":sha256:")
        .append(path.getSha256())
        .append(":md5:")
        .append(path.getMd5());
    return FileHasher.getSha(sb.toString());
  }

}
