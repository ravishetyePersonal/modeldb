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
import java.util.HashSet;
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
      if (locationList.isEmpty()) { // getting root
        Folder folder = getFolder(session, commit.getCommit_hash(), folderHash);
        session.getTransaction().commit();
        return GetCommitComponentRequest.Response.newBuilder().setFolder(folder).build();
      }
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
   * @return {@link List<InternalFolderElementEntity>}
   */
  private List<InternalFolderElementEntity> getFolderElement(
      Session session, String parentFolderHash, String elementName) {
    StringBuilder folderQueryHQLBuilder =
        new StringBuilder("From ")
            .append(InternalFolderElementEntity.class.getSimpleName())
            .append(" parentIfe WHERE parentIfe.folder_hash = :folderHash ");

    if (elementName != null && !elementName.isEmpty()) {
      folderQueryHQLBuilder.append("AND parentIfe.element_name = :elementName");
    }

    Query<InternalFolderElementEntity> fetchTreeQuery =
        session.createQuery(folderQueryHQLBuilder.toString());
    fetchTreeQuery.setParameter("folderHash", parentFolderHash);
    if (elementName != null && !elementName.isEmpty()) {
      fetchTreeQuery.setParameter("elementName", elementName);
    }
    return fetchTreeQuery.list();
  }

  boolean childContains(Set<?> list, Set<?> sublist) {
    return Collections.indexOfSubList(new LinkedList<>(list), new LinkedList<>(sublist)) != -1;
  }

  private Set<BlobExpanded> getChildFolderBlobList(
      Session session,
      List<String> requestedLocation,
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

    Set<BlobExpanded> childBlobExpanded = new LinkedHashSet<>();
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

  Set<BlobExpanded> getCommitBlobList(Session session, String folderHash, List<String> locationList)
      throws ModelDBException {

    String parentLocation = locationList.size() == 0 ? null : locationList.get(0);
    List<InternalFolderElementEntity> parentFolderElementList =
        getFolderElement(session, folderHash, parentLocation);
    if (parentFolderElementList == null || parentFolderElementList.isEmpty()) {
      throw new ModelDBException("No such folder found : " + parentLocation, Status.Code.NOT_FOUND);
    }

    Set<BlobExpanded> finalLocationBlobList = new LinkedHashSet<>();
    for (InternalFolderElementEntity parentFolderElement : parentFolderElementList) {
      if (!parentFolderElement.getElement_type().equals(TREE)) {
        Blob blob = getBlob(session, parentFolderElement);
        BlobExpanded blobExpanded =
            BlobExpanded.newBuilder()
                .addLocation(parentFolderElement.getElement_name())
                .setBlob(blob)
                .build();
        finalLocationBlobList.add(blobExpanded);
      } else {
        Set<String> location = new LinkedHashSet<>();
        Set<BlobExpanded> locationBlobList =
            getChildFolderBlobList(session, locationList, location, folderHash);
        finalLocationBlobList.addAll(locationBlobList);
      }
    }
    return finalLocationBlobList;
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
      Set<BlobExpanded> locationBlobList =
          getCommitBlobList(session, commit.getRootSha(), locationList);
      return ListCommitBlobsRequest.Response.newBuilder().addAllBlobs(locationBlobList).build();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      if (throwable instanceof ModelDBException) {
        throw (ModelDBException) throwable;
      }
      throw new ModelDBException("Unknown error", Status.Code.INTERNAL);
    }
  }

  @Override
  public ComputeRepositoryDiffRequest.Response computeRepositoryDiff(
      RepositoryFunction repositoryFunction, ComputeRepositoryDiffRequest request)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repositoryEntity = repositoryFunction.apply(session);

      CommitEntity internalCommitA = session.get(CommitEntity.class, request.getCommitA());
      if (internalCommitA == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitA(), Status.Code.NOT_FOUND);
      }

      CommitEntity internalCommitB = session.get(CommitEntity.class, request.getCommitB());
      if (internalCommitB == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitB(), Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          session, internalCommitA.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitA.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          session, internalCommitB.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitB.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      Set<BlobExpanded> locationBlobListCommitA =
          getCommitBlobList(session, internalCommitA.getRootSha(), new ArrayList<>());
      Map<String, BlobExpanded> locationBlobsMapCommitA =
          getLocationWiseBlobExpandedMapFromList(locationBlobListCommitA);

      Set<BlobExpanded> locationBlobListCommitB =
          getCommitBlobList(session, internalCommitB.getRootSha(), new ArrayList<>());
      Map<String, BlobExpanded> locationBlobsMapCommitB =
          getLocationWiseBlobExpandedMapFromList(locationBlobListCommitB);

      session.getTransaction().commit();

      // Added new blob location in the CommitA
      Set<String> addedLocations = new HashSet<>(locationBlobsMapCommitB.keySet());
      addedLocations.addAll(locationBlobsMapCommitA.keySet());
      addedLocations.removeAll(locationBlobsMapCommitB.keySet());
      LOGGER.debug("Added location for Diff : {}", addedLocations);

      // deleted new blob location from the CommitA
      Set<String> deletedLocations = new HashSet<>(locationBlobsMapCommitA.keySet());
      deletedLocations.addAll(locationBlobsMapCommitB.keySet());
      deletedLocations.removeAll(locationBlobsMapCommitA.keySet());
      LOGGER.debug("Deleted location for Diff : {}", deletedLocations);

      // deleted new blob location from the CommitA
      Set<String> modifiedLocations = new HashSet<>(locationBlobsMapCommitA.keySet());
      modifiedLocations.removeAll(addedLocations);
      LOGGER.debug("Modified location for Diff : {}", modifiedLocations);

      List<BlobDiff> addedBlobDiffList = getAddedBlobDiff(addedLocations, locationBlobsMapCommitA);
      List<BlobDiff> deletedBlobDiffList =
          getDeletedBlobDiff(deletedLocations, locationBlobsMapCommitB);
      List<BlobDiff> modifiedBlobDiffList =
          getModifiedBlobDiff(modifiedLocations, locationBlobsMapCommitA, locationBlobsMapCommitB);

      return ComputeRepositoryDiffRequest.Response.newBuilder()
          .addAllDiffs(addedBlobDiffList)
          .addAllDiffs(deletedBlobDiffList)
          .addAllDiffs(modifiedBlobDiffList)
          .build();
    }
  }

  List<BlobDiff> getAddedBlobDiff(
      Set<String> addedLocations, Map<String, BlobExpanded> locationBlobsMapCommitA) {
    return addedLocations.stream()
        .map(
            location -> {
              BlobExpanded blobExpanded = locationBlobsMapCommitA.get(location);
              Blob blob = blobExpanded.getBlob();
              switch (blob.getContentCase()) {
                case DATASET:
                  switch (blob.getDataset().getContentCase()) {
                    case PATH:
                      PathDatasetBlob pathDatasetBlob = blob.getDataset().getPath();
                      PathDatasetDiff pathDatasetDiff =
                          PathDatasetDiff.newBuilder().setA(pathDatasetBlob).setAdded(true).build();
                      DatasetDiff datasetDiff =
                          DatasetDiff.newBuilder().setPath(pathDatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(datasetDiff)
                          .build();
                    case S3:
                      S3DatasetBlob s3DatasetBlob = blob.getDataset().getS3();
                      PathDatasetBlob.Builder s3PathDatasetBlob = PathDatasetBlob.newBuilder();
                      s3DatasetBlob
                          .getComponentsList()
                          .forEach(
                              s3DatasetComponentBlob ->
                                  s3PathDatasetBlob
                                      .addComponents(s3DatasetComponentBlob.getPath())
                                      .build());
                      PathDatasetDiff s3PathDatasetDiff =
                          PathDatasetDiff.newBuilder()
                              .setA(s3PathDatasetBlob)
                              .setAdded(true)
                              .build();
                      S3DatasetDiff s3DatasetDiff =
                          S3DatasetDiff.newBuilder().setPath(s3PathDatasetDiff).build();
                      DatasetDiff finalS3DatasetDiff =
                          DatasetDiff.newBuilder().setS3(s3DatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(finalS3DatasetDiff)
                          .build();
                  }
                  break;
                  // TODO: Implement Diff logic after main functionality done
                case ENVIRONMENT:
                case CODE:
                case CONFIG:
                  break;
              }
              return BlobDiff.newBuilder().build();
            })
        .collect(Collectors.toList());
  }

  List<BlobDiff> getDeletedBlobDiff(
      Set<String> deletedLocations, Map<String, BlobExpanded> locationBlobsMapCommitB) {
    return deletedLocations.stream()
        .map(
            location -> {
              BlobExpanded blobExpanded = locationBlobsMapCommitB.get(location);
              Blob blob = blobExpanded.getBlob();
              switch (blob.getContentCase()) {
                case DATASET:
                  switch (blob.getDataset().getContentCase()) {
                    case PATH:
                      PathDatasetBlob pathDatasetBlob = blob.getDataset().getPath();
                      PathDatasetDiff pathDatasetDiff =
                          PathDatasetDiff.newBuilder()
                              .setB(pathDatasetBlob)
                              .setDeleted(true)
                              .build();
                      DatasetDiff datasetDiff =
                          DatasetDiff.newBuilder().setPath(pathDatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(datasetDiff)
                          .build();
                    case S3:
                      S3DatasetBlob s3DatasetBlob = blob.getDataset().getS3();
                      PathDatasetBlob.Builder s3PathDatasetBlob = PathDatasetBlob.newBuilder();
                      s3DatasetBlob
                          .getComponentsList()
                          .forEach(
                              s3DatasetComponentBlob ->
                                  s3PathDatasetBlob
                                      .addComponents(s3DatasetComponentBlob.getPath())
                                      .build());
                      PathDatasetDiff s3PathDatasetDiff =
                          PathDatasetDiff.newBuilder()
                              .setB(s3PathDatasetBlob)
                              .setDeleted(true)
                              .build();
                      S3DatasetDiff s3DatasetDiff =
                          S3DatasetDiff.newBuilder().setPath(s3PathDatasetDiff).build();
                      DatasetDiff finalS3DatasetDiff =
                          DatasetDiff.newBuilder().setS3(s3DatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(finalS3DatasetDiff)
                          .build();
                  }
                  break;
                  // TODO: Implement Diff logic after main functionality done
                case ENVIRONMENT:
                case CODE:
                case CONFIG:
                  break;
              }
              return BlobDiff.newBuilder().build();
            })
        .collect(Collectors.toList());
  }

  List<BlobDiff> getModifiedBlobDiff(
      Set<String> modifiedLocations,
      Map<String, BlobExpanded> locationBlobsMapCommitA,
      Map<String, BlobExpanded> locationBlobsMapCommitB) {
    return modifiedLocations.stream()
        .map(
            location -> {
              BlobExpanded blobExpandedCommitA = locationBlobsMapCommitA.get(location);
              BlobExpanded blobExpandedCommitB = locationBlobsMapCommitB.get(location);
              Blob blobCommitA = blobExpandedCommitA.getBlob();
              Blob blobCommitB = blobExpandedCommitB.getBlob();
              switch (blobCommitA.getContentCase()) {
                case DATASET:
                  switch (blobCommitA.getDataset().getContentCase()) {
                    case PATH:
                      PathDatasetBlob pathDatasetBlobCommitA = blobCommitA.getDataset().getPath();
                      PathDatasetBlob pathDatasetBlobCommitB = blobCommitB.getDataset().getPath();
                      PathDatasetDiff pathDatasetDiff =
                          PathDatasetDiff.newBuilder()
                              .setA(pathDatasetBlobCommitA)
                              .setB(pathDatasetBlobCommitB)
                              .build();
                      DatasetDiff datasetDiff =
                          DatasetDiff.newBuilder().setPath(pathDatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(datasetDiff)
                          .build();
                    case S3:
                      S3DatasetBlob s3DatasetBlobCommitA = blobCommitA.getDataset().getS3();
                      PathDatasetBlob.Builder s3PathDatasetBlobCommitA =
                          PathDatasetBlob.newBuilder();
                      s3DatasetBlobCommitA
                          .getComponentsList()
                          .forEach(
                              s3DatasetComponentBlob ->
                                  s3PathDatasetBlobCommitA
                                      .addComponents(s3DatasetComponentBlob.getPath())
                                      .build());

                      S3DatasetBlob s3DatasetBlobCommitB = blobCommitB.getDataset().getS3();
                      PathDatasetBlob.Builder s3PathDatasetBlobCommitB =
                          PathDatasetBlob.newBuilder();
                      s3DatasetBlobCommitB
                          .getComponentsList()
                          .forEach(
                              s3DatasetComponentBlob ->
                                  s3PathDatasetBlobCommitB
                                      .addComponents(s3DatasetComponentBlob.getPath())
                                      .build());

                      PathDatasetDiff s3PathDatasetDiff =
                          PathDatasetDiff.newBuilder()
                              .setA(s3PathDatasetBlobCommitA)
                              .setB(s3PathDatasetBlobCommitB)
                              .build();
                      S3DatasetDiff s3DatasetDiff =
                          S3DatasetDiff.newBuilder().setPath(s3PathDatasetDiff).build();
                      DatasetDiff finalS3DatasetDiff =
                          DatasetDiff.newBuilder().setS3(s3DatasetDiff).build();
                      return BlobDiff.newBuilder()
                          // TODO: Here used the `#` for split the locations but if folder locations
                          // TODO: - contain the `#` then this functionality will break.
                          .addAllLocation(Arrays.asList(location.split("#")))
                          .setDataset(finalS3DatasetDiff)
                          .build();
                  }
                  break;
                  // TODO: Implement Diff logic after main functionality done
                case ENVIRONMENT:
                case CODE:
                case CONFIG:
                  break;
              }
              return BlobDiff.newBuilder().build();
            })
        .collect(Collectors.toList());
  }

  private Map<String, BlobExpanded> getLocationWiseBlobExpandedMapFromList(
      Set<BlobExpanded> blobExpandeds) {
    return blobExpandeds.stream()
        .collect(
            Collectors.toMap(
                // TODO: Here used the `#` for joining the locations but if folder locations contain
                // TODO: - the `#` then this functionality will break.
                blobExpanded -> String.join("#", blobExpanded.getLocationList()),
                blobExpanded -> blobExpanded));
  }
}
