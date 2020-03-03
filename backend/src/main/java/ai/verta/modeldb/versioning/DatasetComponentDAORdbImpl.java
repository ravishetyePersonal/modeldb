package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.ComponentEntity;
import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import com.google.protobuf.ProtocolStringList;
import io.grpc.Status;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DatasetComponentDAORdbImpl implements DatasetComponentDAO {
  private static final Logger LOGGER = LogManager.getLogger(DatasetComponentDAORdbImpl.class);

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
   *
   * @throws ModelDBException
   */
  @Override
  public String setBlobs(Session session, List<BlobExpanded> blobsList, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException {
    TreeElem rootTree = new TreeElem();
    for (BlobExpanded blob : blobsList) {
      TreeElem treeElem =
          rootTree.children.getOrDefault(blob.getLocationList().get(0), new TreeElem());
      Status statusMessage;
      switch (blob.getBlob().getContentCase()) {
        case DATASET:
          processDataset(blob, treeElem, fileHasher, getBlobType(blob));
          break;
        case ENVIRONMENT:
          throw new ModelDBException(
              "Not supported yet " + blob.getBlob().getContentCase(), Status.Code.UNIMPLEMENTED);
        case CONTENT_NOT_SET:
        default:
          throw new ModelDBException(
              "Unknown blob type found " + blob.getBlob().getContentCase(), Status.Code.UNKNOWN);
      }
      rootTree.children.putIfAbsent(treeElem.path, treeElem);
    }
    final InternalFolderElement internalFolderElement = rootTree.saveFolders(session, fileHasher);
    return internalFolderElement.getElementSha();
  }

  private String getBlobType(BlobExpanded blob) throws ModelDBException {
    Status statusMessage;
    switch (blob.getBlob().getContentCase()) {
      case DATASET:
        switch (blob.getBlob().getDataset().getContentCase()) {
          case PATH:
            return PathDatasetBlob.class.getSimpleName();
          case S3:
            return S3DatasetBlob.class.getSimpleName();
          case CONTENT_NOT_SET:
          default:
            throw new ModelDBException(
                "Unknown dataset type found " + blob.getBlob().getDataset().getContentCase(),
                Status.Code.UNKNOWN);
        }
      case ENVIRONMENT:
        throw new ModelDBException(
            "Not supported yet " + blob.getBlob().getContentCase(), Status.Code.UNIMPLEMENTED);
      case CONTENT_NOT_SET:
      default:
        throw new ModelDBException(
            "Unknown blob type found " + blob.getBlob().getContentCase(), Status.Code.UNKNOWN);
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
              new S3DatasetComponentBlobEntity(sha256, componentBlob);
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

  private Blob getBlob(Session session, InternalFolderElementEntity folderElementEntity)
      throws ModelDBException {
    DatasetBlob.Builder datasetBlobBuilder = DatasetBlob.newBuilder();
    switch (folderElementEntity.getElement_type()) {
      case "S3DatasetBlob":
        String s3ComponentQueryHQL =
            "From "
                + S3DatasetComponentBlobEntity.class.getSimpleName()
                + " s3 WHERE s3.id.s3_dataset_blob_id = :blobShas";

        Query<S3DatasetComponentBlobEntity> s3ComponentQuery =
            session.createQuery(s3ComponentQueryHQL);
        s3ComponentQuery.setParameter("blobShas", folderElementEntity.getElement_sha());
        List<S3DatasetComponentBlobEntity> datasetComponentBlobEntities = s3ComponentQuery.list();

        if (datasetComponentBlobEntities != null && datasetComponentBlobEntities.size() > 0) {
          List<S3DatasetComponentBlob> componentBlobs =
              datasetComponentBlobEntities.stream()
                  .map(S3DatasetComponentBlobEntity::toProto)
                  .collect(Collectors.toList());
          datasetBlobBuilder.setS3(
              S3DatasetBlob.newBuilder().addAllComponents(componentBlobs).build());
          return Blob.newBuilder().setDataset(datasetBlobBuilder.build()).build();
        } else {
          throw new ModelDBException("Blob not found", Status.Code.NOT_FOUND);
        }
      case "PathDatasetBlob":
        String pathComponentQueryHQL =
            "From "
                + PathDatasetComponentBlobEntity.class.getSimpleName()
                + " p WHERE p.id.path_dataset_blob_id = :blobShas";

        Query<PathDatasetComponentBlobEntity> pathComponentQuery =
            session.createQuery(pathComponentQueryHQL);
        pathComponentQuery.setParameter("blobShas", folderElementEntity.getElement_sha());
        List<PathDatasetComponentBlobEntity> pathDatasetComponentBlobEntities =
            pathComponentQuery.list();

        if (pathDatasetComponentBlobEntities != null
            && pathDatasetComponentBlobEntities.size() > 0) {
          List<PathDatasetComponentBlob> componentBlobs =
              pathDatasetComponentBlobEntities.stream()
                  .map(PathDatasetComponentBlobEntity::toProto)
                  .collect(Collectors.toList());
          datasetBlobBuilder.setPath(
              PathDatasetBlob.newBuilder().addAllComponents(componentBlobs).build());
          return Blob.newBuilder().setDataset(datasetBlobBuilder.build()).build();
        } else {
          throw new ModelDBException("Blob not found", Status.Code.NOT_FOUND);
        }
      case "PythonEnvironmentBlob":
      case "DockerEnvironmentBlob":
        throw new ModelDBException("Not Implemented", Status.Code.UNIMPLEMENTED);
      default:
        throw new ModelDBException(
            "Unknown blob type found " + folderElementEntity.getElement_type(),
            Status.Code.UNKNOWN);
    }
  }

  private Folder getFolder(Session session, String commitSha, String folderSha) throws Throwable {
    Optional result =
        session
            .createQuery(
                "From "
                    + InternalFolderElementEntity.class.getSimpleName()
                    + " where folder_hash = '"
                    + folderSha
                    + "'")
            .list().stream()
            .map(
                d -> {
                  InternalFolderElementEntity entity = (InternalFolderElementEntity) d;
                  Folder.Builder folder = Folder.newBuilder();
                  FolderElement.Builder folderElement =
                      FolderElement.newBuilder()
                          .setElementName(entity.getElement_name())
                          .setCreatedByCommit(commitSha);

                  if (entity.getElement_type().equals(TREE)) {
                    folder.addSubFolders(folderElement);
                  } else {
                    folder.addBlobs(folderElement);
                  }
                  return folder.build();
                })
            .reduce((a, b) -> ((Folder) a).toBuilder().mergeFrom((Folder) b).build());

    return (Folder)
        result.orElseThrow(
            () -> new ModelDBException("No such folder found", Status.Code.NOT_FOUND));
  }

  // TODO : check if there is a way to optimize on the calls to data base.
  // We should fetch data  in a single query.
  @Override
  public GetCommitComponentRequest.Response getCommitComponent(
      RepositoryFunction repositoryFunction, String commitHash, ProtocolStringList locationList)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository = repositoryFunction.apply(session);
      CommitEntity commit = session.get(CommitEntity.class, commitHash);

      if (commit == null) {
        throw new ModelDBException("No such commit", Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(session, commitHash, repository.getId())) {
        throw new ModelDBException("No such commit found in the repository", Status.Code.NOT_FOUND);
      }

      String folderHash = commit.getRootSha();
      for (int index = 0; index < locationList.size(); index++) {
        String folderLocation = locationList.get(index);
        String folderQueryHQL =
            "From "
                + InternalFolderElementEntity.class.getSimpleName()
                + " parentIfe WHERE parentIfe.element_name = :location AND parentIfe.folder_hash = :folderHash";
        Query<InternalFolderElementEntity> fetchTreeQuery = session.createQuery(folderQueryHQL);
        fetchTreeQuery.setParameter("location", folderLocation);
        fetchTreeQuery.setParameter("folderHash", folderHash);
        InternalFolderElementEntity elementEntity = fetchTreeQuery.uniqueResult();

        if (elementEntity == null) {
          LOGGER.warn(
              "No such folder found : {}. Failed at index {} looking for {}",
              folderLocation,
              index,
              folderLocation);
          throw new ModelDBException(
              "No such folder found : " + folderLocation, Status.Code.NOT_FOUND);
        }
        if (elementEntity.getElement_type().equals(TREE)) {
          folderHash = elementEntity.getElement_sha();
          if (index == locationList.size() - 1) {
            Folder folder = getFolder(session, commit.getCommit_hash(), folderHash);
            session.getTransaction().commit();
            return GetCommitComponentRequest.Response.newBuilder().setFolder(folder).build();
          }
        } else {
          if (index == locationList.size() - 1) {
            Blob blob = getBlob(session, elementEntity);
            session.getTransaction().commit();
            return GetCommitComponentRequest.Response.newBuilder().setBlob(blob).build();
          } else {
            throw new ModelDBException(
                "No such folder found : " + locationList.get(index + 1), Status.Code.NOT_FOUND);
          }
        }
      }
    } catch (Throwable throwable) {
      if (throwable instanceof ModelDBException) {
        throw (ModelDBException) throwable;
      }
      LOGGER.warn(throwable);
      throw new ModelDBException("Unknown error", Status.Code.INTERNAL);
    }
    throw new ModelDBException(
        "Unexpected logic issue found when fetching blobs", Status.Code.UNKNOWN);
  }

  /**
   * get the Folder Element pointed to by the parentFolderHash and elementName
   *
   * @param session
   * @param parentFolderHash : folder hash of the parent
   * @param elementName : element name of the element to be fetched
   * @return {@link InternalFolderElementEntity}
   */
  private InternalFolderElementEntity getFolderElement(
      Session session, String parentFolderHash, String elementName) {
    String folderQueryHQL =
        "From "
            + InternalFolderElementEntity.class.getSimpleName()
            + " parentIfe WHERE parentIfe.folder_hash = :folderHash AND parentIfe.element_name = :elementName";
    Query<InternalFolderElementEntity> fetchTreeQuery = session.createQuery(folderQueryHQL);
    fetchTreeQuery.setParameter("folderHash", parentFolderHash);
    fetchTreeQuery.setParameter("elementName", elementName);
    return fetchTreeQuery.uniqueResult();
  }

  boolean childContains(Set<?> list, Set<?> sublist) {
    return Collections.indexOfSubList(new LinkedList<>(list), new LinkedList<>(sublist)) != -1;
  }

  private List<BlobExpanded> getChildFolderBlobList(
      Session session,
      ProtocolStringList requestedLocation,
      Set<String> parentLocation,
      String parentFolderHash)
      throws ModelDBException {
    String folderQueryHQL =
        "From "
            + InternalFolderElementEntity.class.getSimpleName()
            + " parentIfe WHERE parentIfe.folder_hash = :folderHash";
    Query<InternalFolderElementEntity> fetchTreeQuery = session.createQuery(folderQueryHQL);
    fetchTreeQuery.setParameter("folderHash", parentFolderHash);
    List<InternalFolderElementEntity> childElementFolders = fetchTreeQuery.list();

    List<BlobExpanded> childBlobExpanded = new ArrayList<>();
    for (InternalFolderElementEntity childElementFolder : childElementFolders) {
      if (childElementFolder.getElement_type().equals(TREE)) {
        Set<String> childLocation = new LinkedHashSet<>(parentLocation);
        childLocation.add(childElementFolder.getElement_name());
        if (childContains(new LinkedHashSet<>(requestedLocation), childLocation)
            || childLocation.containsAll(requestedLocation)) {
          childBlobExpanded.addAll(
              getChildFolderBlobList(
                  session, requestedLocation, childLocation, childElementFolder.getElement_sha()));
        }
      } else {
        if (parentLocation.containsAll(requestedLocation)) {
          Blob blob = getBlob(session, childElementFolder);
          BlobExpanded blobExpanded =
              BlobExpanded.newBuilder()
                  .addAllLocation(parentLocation)
                  .addLocation(childElementFolder.getElement_name())
                  .setBlob(blob)
                  .build();
          childBlobExpanded.add(blobExpanded);
        }
      }
    }
    return childBlobExpanded;
  }

  @Override
  public ListCommitBlobsRequest.Response getCommitBlobsList(
      RepositoryFunction repositoryFunction, String commitHash, ProtocolStringList locationList)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();

      CommitEntity commit = session.get(CommitEntity.class, commitHash);
      if (commit == null) {
        throw new ModelDBException("No such commit", Status.Code.NOT_FOUND);
      }

      RepositoryEntity repository = repositoryFunction.apply(session);
      if (!VersioningUtils.commitRepositoryMappingExists(session, commitHash, repository.getId())) {
        throw new ModelDBException("No such commit found in the repository", Status.Code.NOT_FOUND);
      }

      String folderHash = commit.getRootSha();

      InternalFolderElementEntity parentFolderElement =
          getFolderElement(session, folderHash, locationList.get(0));
      if (parentFolderElement == null) {
        throw new ModelDBException(
            "No such folder found : " + locationList.get(0), Status.Code.NOT_FOUND);
      }

      if (!parentFolderElement.getElement_type().equals(TREE)) {
        Blob blob = getBlob(session, parentFolderElement);
        BlobExpanded blobExpanded =
            BlobExpanded.newBuilder()
                .addLocation(parentFolderElement.getElement_name())
                .setBlob(blob)
                .build();
        return ListCommitBlobsRequest.Response.newBuilder().addBlobs(blobExpanded).build();
      } else {
        Set<String> location = new LinkedHashSet<>();
        List<BlobExpanded> locationBlobList =
            getChildFolderBlobList(session, locationList, location, folderHash);
        session.getTransaction().commit();
        return ListCommitBlobsRequest.Response.newBuilder().addAllBlobs(locationBlobList).build();
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      if (throwable instanceof ModelDBException) {
        throw (ModelDBException) throwable;
      }
      throw new ModelDBException("Unknown error", Status.Code.INTERNAL);
    }
  }
}
