package ai.verta.modeldb.versioning;

import ai.verta.modeldb.entities.ComponentEntity;
import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.cfg.NotYetImplementedException;

public class DatasetComponentDAORdbImpl implements DatasetComponentDAO {

  public static final String TREE = "TREE";

  static class TreeElem {
    String path;
    String sha256 = null;
    String type = null;
    ComponentEntity componentEntity;
    Map<String, TreeElem> children = new HashMap<>();

    TreeElem() {}

    TreeElem push(
        List<String> pathList, String sha256, String type, ComponentEntity componentEntity) {
      path = pathList.get(0);
      if (pathList.size() > 1) {
        children.putIfAbsent(pathList.get(1), new TreeElem());
        if (this.type == null) this.type = TREE;
        return children
            .get(pathList.get(1))
            .push(pathList.subList(1, pathList.size()), sha256, type, componentEntity);
      } else {
        this.sha256 = sha256;
        this.type = type;
        this.componentEntity = componentEntity;
        return this;
      }
    }

    String getPath() {
      return path != null ? path : "";
    }

    String getSha256() {
      return sha256;
    }

    String getType() {
      return type;
    }

    public ComponentEntity getComponentEntity() {
      return componentEntity;
    }

    InternalFolderElement saveFolders(Session session, FileHasher fileHasher)
        throws NoSuchAlgorithmException {
      if (children.isEmpty()) {
        return InternalFolderElement.newBuilder()
            .setElementName(getPath())
            .setElementSha(getSha256())
            .build();
      } else {
        InternalFolder.Builder internalFolder = InternalFolder.newBuilder();
        List<InternalFolderElement> elems = new LinkedList<>();
        for (TreeElem elem : children.values()) {
          InternalFolderElement build = elem.saveFolders(session, fileHasher);
          elems.add(build);
          if (elem.getType().equals(TREE)) {
            internalFolder.addSubFolders(build);
          } else {
            internalFolder.addBlobs(build);
          }
        }
        final InternalFolderElement treeBuild =
            InternalFolderElement.newBuilder()
                .setElementName(getPath())
                .setElementSha(fileHasher.getSha(internalFolder.build()))
                .build();
        Iterator<TreeElem> iter = children.values().iterator();
        for (InternalFolderElement elem : elems) {
          final TreeElem next = iter.next();
          session.saveOrUpdate(
              new ConnectionBuilder(
                      elem, treeBuild.getElementSha(), next.getType(), next.getComponentEntity())
                  .build());
        }
        return treeBuild;
      }
    }

    private class ConnectionBuilder {
      private final InternalFolderElement elem;
      private final String baseBlobHash;
      private final String type;
      private final ComponentEntity componentEntity;

      public ConnectionBuilder(
          InternalFolderElement elem,
          String folderHash,
          String type,
          ComponentEntity componentEntity) {
        this.elem = elem;
        this.baseBlobHash = folderHash;
        this.type = type;
        this.componentEntity = componentEntity;
      }

      public Object build() {
        if (componentEntity != null) {
          componentEntity.setBaseBlobHash(baseBlobHash);
          return componentEntity;
        }
        return new InternalFolderElementEntity(elem, baseBlobHash, type);
      }
    }
  }

  /**
   * Goes through each BlobExpanded creating TREE/BLOB node top down and computing SHA bottom up
   * there is a rootSHA which holds one TREE node of each BlobExpanded
   */
  @Override
  public String setBlobs(Session session, List<BlobExpanded> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException {
    TreeElem rootTree = new TreeElem();
    for (BlobExpanded blob : blobsList) {
      TreeElem treeElem =
          rootTree.children.getOrDefault(blob.getLocationList().get(0), new TreeElem());
      switch (blob.getBlob().getContentCase()) {
        case DATASET:
          processDataset(blob, treeElem, fileHasher, getBlobType(blob));
          break;
        case ENVIRONMENT:
          throw new NotYetImplementedException(
              "not supported yet"); // TODO EL/AJ to throw right exceptions
        case CONTENT_NOT_SET:
        default:
          throw new IllegalStateException(
              "unexpected Dataset type"); // TODO EL/AJ to throw right exceptions
      }
      rootTree.children.putIfAbsent(treeElem.path, treeElem);
    }
    final InternalFolderElement internalFolderElement = rootTree.saveFolders(session, fileHasher);
    return internalFolderElement.getElementSha();
  }

  private String getBlobType(BlobExpanded blob) {
    switch (blob.getBlob().getContentCase()) {
      case DATASET:
        switch (blob.getBlob().getDataset().getContentCase()) {
          case PATH:
            return PathDatasetBlob.class.getSimpleName();
          case S3:
            return S3DatasetBlob.class.getSimpleName();
          case CONTENT_NOT_SET:
          default:
            throw new IllegalStateException(
                "unexpected Dataset type"); // TODO EL/AJ to throw right exceptions
        }
      case ENVIRONMENT:
        throw new NotYetImplementedException(
            "not supported yet"); // TODO EL/AJ to throw right exceptions
      case CONTENT_NOT_SET:
      default:
        throw new IllegalStateException(
            "unexpected Dataset type"); // TODO EL/AJ to throw right exceptions
    }
  }

  /**
   * @param blob : a commit is a collection of multiple BlobExpanded
   * @param treeElem : Each blob or folder need to be converted to a tree element. the process is
   *     bootstrapped with an empty tree for each BlobExpanded
   * @param fileHasher
   * @param blobType
   * @throws NoSuchAlgorithmException
   */
  private void processDataset(
      BlobExpanded blob, TreeElem treeElem, FileHasher fileHasher, String blobType)
      throws NoSuchAlgorithmException {
    final DatasetBlob dataset = blob.getBlob().getDataset();
    final List<String> locationList = blob.getLocationList();

    TreeElem treeChild =
        treeElem.push(
            locationList,
            fileHasher.getSha(dataset),
            blobType,
            null); // need to ensure dataset is sorted
    switch (dataset.getContentCase()) {
      case S3:
        for (S3DatasetComponentBlob componentBlob : dataset.getS3().getComponentsList()) {
          final String sha256 = computeSHA(componentBlob);
          S3DatasetComponentBlobEntity s3DatasetComponentBlobEntity =
              new S3DatasetComponentBlobEntity(
                  sha256, componentBlob.getPath()); // why is UUID required?
          treeChild.push(
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
          treeChild.push(
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
