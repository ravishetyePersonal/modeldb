package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.blob.BlobContainer;
import com.google.protobuf.ProtocolStringList;
import io.grpc.Status;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class BlobDAORdbImpl implements DatasetComponentDAO {
  private static final Logger LOGGER = LogManager.getLogger(BlobDAORdbImpl.class);

  public static final String TREE = "TREE";

  /**
   * Goes through each BlobExpanded creating TREE/BLOB node top down and computing SHA bottom up
   * there is a rootSHA which holds one TREE node of each BlobExpanded
   *
   * @throws ModelDBException
   */
  @Override
  public String setBlobs(Session session, List<BlobContainer> blobContainers, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException {
    TreeElem rootTree = new TreeElem();
    for (BlobContainer blobContainer : blobContainers) {
      blobContainer.process(session, rootTree, fileHasher);
    }
    final InternalFolderElement internalFolderElement = rootTree.saveFolders(session, fileHasher);
    return internalFolderElement.getElementSha();
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
}
