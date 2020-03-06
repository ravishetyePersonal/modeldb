package ai.verta.modeldb.versioning;

import ai.verta.modeldb.entities.BlobTreeInformation;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class TreeElem {
  String path;
  String blobHash = null;
  String type = null;
  BlobTreeInformation blobTreeInformation;
  Map<String, TreeElem> children = new HashMap<>();

  TreeElem() {}

  public TreeElem push(
      List<String> pathList,
      String blobHash,
      String type,
      BlobTreeInformation blobTreeInformation) {
    path = pathList.get(0);
    if (pathList.size() > 1) {
      children.putIfAbsent(pathList.get(1), new TreeElem());
      if (this.type == null) this.type = BlobDAORdbImpl.TREE;
      return children
          .get(pathList.get(1))
          .push(pathList.subList(1, pathList.size()), blobHash, type, blobTreeInformation);
    } else {
      this.blobHash = blobHash;
      this.type = type;
      this.blobTreeInformation = blobTreeInformation;
      return this;
    }
  }

  String getPath() {
    return path != null ? path : "";
  }

  String getBlobHash() {
    return blobHash;
  }

  String getType() {
    return type;
  }

  public BlobTreeInformation getBlobTreeInformation() {
    return blobTreeInformation;
  }

  InternalFolderElement saveFolders(Session session, FileHasher fileHasher)
      throws NoSuchAlgorithmException {
    if (children.isEmpty()) {
      return InternalFolderElement.newBuilder()
          .setElementName(getPath())
          .setElementSha(getBlobHash())
          .build();
    } else {
      InternalFolder.Builder internalFolder = InternalFolder.newBuilder();
      List<InternalFolderElement> elems = new LinkedList<>();
      for (TreeElem elem : children.values()) {
        InternalFolderElement build = elem.saveFolders(session, fileHasher);
        elems.add(build);
        if (elem.getType().equals(BlobDAORdbImpl.TREE)) {
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
                    elem, treeBuild.getElementSha(), next.getType(), next.getBlobTreeInformation())
                .build());
      }
      return treeBuild;
    }
  }

  private class ConnectionBuilder {
    private final InternalFolderElement elem;
    private final String baseBlobHash;
    private final String type;
    private final BlobTreeInformation blobTreeInformation;

    public ConnectionBuilder(
        InternalFolderElement elem,
        String folderHash,
        String type,
        BlobTreeInformation blobTreeInformation) {
      this.elem = elem;
      this.baseBlobHash = folderHash;
      this.type = type;
      this.blobTreeInformation = blobTreeInformation;
    }

    public Object build() {
      if (blobTreeInformation != null) {
        if (blobTreeInformation.hasComponents()) {
          blobTreeInformation.setBaseBlobHash(baseBlobHash);
          return blobTreeInformation;
        } else {
          return new InternalFolderElementEntity(
              baseBlobHash,
              blobTreeInformation.getElementSha(),
              type,
              blobTreeInformation.getElementName());
        }
      }
      return new InternalFolderElementEntity(elem, baseBlobHash, type);
    }
  }
}
