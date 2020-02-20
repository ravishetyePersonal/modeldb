package ai.verta.modeldb.versioning;

import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.hibernate.Session;

public class DatasetComponentDAORdbImpl implements DatasetComponentDAO {

  static class TreeElem {

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      TreeElem treeElem = (TreeElem) o;
      return Objects.equals(path, treeElem.path);
    }

    @Override
    public int hashCode() {
      return Objects.hash(path);
    }

    String path;
    String fullPath;
    String sha256;
    String type;
    Set<TreeElem> tree = new HashSet<>();

    TreeElem push(List<String> pathArray, String fullPath, String sha256, String type) {
      path = pathArray.get(0);
      if (pathArray.size() > 1) {
        this.sha256 = null;
        this.type = null;
        TreeElem elem = push(pathArray.subList(1, pathArray.size()), fullPath, sha256, type);
        tree.add(elem);
        return elem;
      } else {
        this.sha256 = sha256;
        this.type = type;
        this.fullPath = fullPath;
        return this;
      }
    }

    String getPath() {
      return fullPath;
    }

    String getSha256() {
      return sha256;
    }

    String getType() {
      return type;
    }

    InternalFolderElement saveFolders(Session session, FileHasher fileHasher)
        throws NoSuchAlgorithmException {
      if (tree.isEmpty()) {
        return InternalFolderElement.newBuilder()
            .setElementName(getPath())
            .setElementSha(getSha256())
            .build();
      } else {
        InternalFolder.Builder internalFolder = InternalFolder.newBuilder();
        List<InternalFolderElement> elems = new LinkedList<>();
        for (TreeElem elem : tree) {
          InternalFolderElement build = elem.saveFolders(session, fileHasher);
          elems.add(build);
          if (elem.getType().equals("Folder")) {
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
        Iterator<TreeElem> iter = tree.iterator();
        for (InternalFolderElement elem : elems) {
          session.saveOrUpdate(
              new InternalFolderElementEntity(elem, build.getElementSha(), iter.next().getType()));
        }
        return build;
      }
    }
  }

  public String setBlobs(Session session, List<BlobExpanded> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException {
    TreeElem treeElem = new TreeElem();
    for (BlobExpanded blob : blobsList) {
      final DatasetBlob dataset = blob.getBlob().getDataset();
      TreeElem treeChild =
          treeElem.push(
              Arrays.asList(blob.getPath().split("/")),
              blob.getPath(),
              fileHasher.getSha(dataset),
              "Folder");
      switch (dataset.getContentCase()) {
        case S3:
          for (S3DatasetComponentBlob componentBlob : dataset.getS3().getComponentsList()) {
            S3DatasetComponentBlobEntity s3DatasetComponentBlobEntity =
                new S3DatasetComponentBlobEntity(
                    UUID.randomUUID().toString(), String.valueOf(componentBlob.hashCode()), componentBlob.getPath());
            session.saveOrUpdate(s3DatasetComponentBlobEntity);
            final String[] split = componentBlob.getPath().getPath().split("/");
            treeChild.push(
                Collections.singletonList(split[split.length - 1]),
                componentBlob.getPath().getPath(),
                componentBlob.getPath().getSha256(),
                componentBlob.getClass().getSimpleName());
          }
          break;
        case PATH:
          for (PathDatasetComponentBlob componentBlob : dataset.getPath().getComponentsList()) {
            PathDatasetComponentBlobEntity pathDatasetComponentBlobEntity =
                new PathDatasetComponentBlobEntity(
                    UUID.randomUUID().toString(), String.valueOf(componentBlob.hashCode()), componentBlob);
            session.saveOrUpdate(pathDatasetComponentBlobEntity);
            final String[] split = componentBlob.getPath().split("/");
            treeChild.push(
                Collections.singletonList(split[split.length - 1]),
                componentBlob.getPath(),
                componentBlob.getSha256(),
                componentBlob.getClass().getSimpleName());
          }
          break;
      }
    }
    final InternalFolderElement internalFolderElement = treeElem.saveFolders(session, fileHasher);
    session.saveOrUpdate(new InternalFolderElementEntity(internalFolderElement, null, "Folder"));
    return internalFolderElement.getElementSha();
  }
}
