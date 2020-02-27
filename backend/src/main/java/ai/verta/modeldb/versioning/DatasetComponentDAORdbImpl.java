package ai.verta.modeldb.versioning;

import ai.verta.modeldb.entities.ComponentEntity;
import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import com.google.protobuf.ProtocolStringList;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.cfg.NotYetImplementedException;

public class DatasetComponentDAORdbImpl implements DatasetComponentDAO {

  public static final String TREE = "TREE";

  static class TreeElem {
    String path;
    String sha256 = null;
    String type = null;
    Map<String, TreeElem> children = new HashMap<>();

    TreeElem() {}

    TreeElem push(List<String> pathList, String sha256, String type) {
      path = pathList.get(0);
      if (pathList.size() > 1) {
        children.putIfAbsent(pathList.get(1), new TreeElem());
        if (this.type == null) this.type = TREE;
        return children
            .get(pathList.get(1))
            .push(pathList.subList(1, pathList.size()), sha256, type);
      } else {
        this.sha256 = sha256;
        this.type = type;
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
        final InternalFolderElement build =
            InternalFolderElement.newBuilder()
                .setElementName(getPath())
                .setElementSha(fileHasher.getSha(internalFolder.build()))
                .build();
        Iterator<TreeElem> iter = children.values().iterator();
        for (InternalFolderElement elem : elems) {
          session.saveOrUpdate(
              new InternalFolderElementEntity(elem, build.getElementSha(), iter.next().getType()));
        }
        return build;
      }
    }
  }

  /** returns the sha */
  @Override
  public String setBlobs(Session session, List<BlobExpanded> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException {
    List<ComponentEntity> componentEntities = new LinkedList<>();
    TreeElem rootTree = new TreeElem();
    for (BlobExpanded blob : blobsList) {
      switch (blob.getBlob().getContentCase()) {
        case DATASET:
          processDataset(session, blob, rootTree, fileHasher, getBlobType(blob), componentEntities);
          break;
        case ENVIRONMENT:
          throw new NotYetImplementedException(
              "not supported yet"); // TODO EL/AJ to throw right exceptions
        case CONTENT_NOT_SET:
        default:
          throw new IllegalStateException(
              "unexpected Dataset type"); // TODO EL/AJ to throw right exceptions
      }
    }
    final InternalFolderElement internalFolderElement = rootTree.saveFolders(session, fileHasher);
    final String elementSha = internalFolderElement.getElementSha();
    for (ComponentEntity componentEntity : componentEntities) {
      session.saveOrUpdate(componentEntity);
    }
    return elementSha;
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

  private void processDataset(
      Session session,
      BlobExpanded blob,
      TreeElem treeElem,
      FileHasher fileHasher,
      String blobType,
      List<ComponentEntity> componentEntities)
      throws NoSuchAlgorithmException {
    final DatasetBlob dataset = blob.getBlob().getDataset();
    final List<String> locationList = blob.getLocationList();
    TreeElem treeChild =
        treeElem.push(
            locationList, fileHasher.getSha(dataset), blobType); // need to ensure dataset is sorted
    switch (dataset.getContentCase()) {
      case S3:
        for (S3DatasetComponentBlob componentBlob : dataset.getS3().getComponentsList()) {
          final String sha256 = computeSHA(componentBlob);
          S3DatasetComponentBlobEntity s3DatasetComponentBlobEntity =
              new S3DatasetComponentBlobEntity(
                  UUID.randomUUID().toString(),
                  sha256,
                  componentBlob.getPath()); // why is UUID required?
          componentEntities.add(s3DatasetComponentBlobEntity);
          treeChild.push(
              Arrays.asList(locationList.get(locationList.size() - 1), componentBlob.getPath().getPath()),
              computeSHA(componentBlob.getPath()),
              componentBlob.getClass().getSimpleName());
        }
        break;
      case PATH:
        for (PathDatasetComponentBlob componentBlob : dataset.getPath().getComponentsList()) {
          final String sha256 = computeSHA(componentBlob);
          PathDatasetComponentBlobEntity pathDatasetComponentBlobEntity =
              new PathDatasetComponentBlobEntity(
                  UUID.randomUUID().toString(), sha256, componentBlob);
          componentEntities.add(pathDatasetComponentBlobEntity);
          treeChild.push(
              Arrays.asList(locationList.get(locationList.size() - 1), componentBlob.getPath()),
              computeSHA(componentBlob),
              componentBlob.getClass().getSimpleName());
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
