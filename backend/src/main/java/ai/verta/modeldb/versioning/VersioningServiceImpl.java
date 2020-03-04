package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBAuthInterceptor;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.authservice.RoleService;
import ai.verta.modeldb.experiment.ExperimentDAO;
import ai.verta.modeldb.experimentRun.ExperimentRunDAO;
import ai.verta.modeldb.monitoring.QPSCountResource;
import ai.verta.modeldb.monitoring.RequestLatencyResource;
import ai.verta.modeldb.utils.ModelDBUtils;
import ai.verta.modeldb.versioning.ListRepositoriesRequest.Response;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob.Builder;
import ai.verta.modeldb.versioning.VersioningServiceGrpc.VersioningServiceImplBase;
import ai.verta.modeldb.versioning.blob.BlobContainer;
import ai.verta.uac.UserInfo;
import io.grpc.Status.Code;
import io.grpc.stub.StreamObserver;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VersioningServiceImpl extends VersioningServiceImplBase {

  private static final Logger LOGGER = LogManager.getLogger(VersioningServiceImpl.class);
  private final AuthService authService;
  private final RoleService roleService;
  private final RepositoryDAO repositoryDAO;
  private final CommitDAO commitDAO;
  private final DatasetComponentDAO datasetComponentDAO;
  private final ExperimentDAO experimentDAO;
  private final ExperimentRunDAO experimentRunDAO;
  private final ModelDBAuthInterceptor modelDBAuthInterceptor;
  private final FileHasher fileHasher;

  public VersioningServiceImpl(
      AuthService authService,
      RoleService roleService,
      RepositoryDAO repositoryDAO,
      CommitDAO commitDAO,
      DatasetComponentDAO datasetComponentDAO,
      ExperimentDAO experimentDAO,
      ExperimentRunDAO experimentRunDAO,
      ModelDBAuthInterceptor modelDBAuthInterceptor,
      FileHasher fileHasher) {
    this.authService = authService;
    this.roleService = roleService;
    this.repositoryDAO = repositoryDAO;
    this.commitDAO = commitDAO;
    this.datasetComponentDAO = datasetComponentDAO;
    this.experimentDAO = experimentDAO;
    this.experimentRunDAO = experimentRunDAO;
    this.modelDBAuthInterceptor = modelDBAuthInterceptor;
    this.fileHasher = fileHasher;
  }

  @Override
  public void listRepositories(
      ListRepositoriesRequest request, StreamObserver<Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        if (request.hasPagination()) {

          if (request.getPagination().getPageLimit() < 1
              && request.getPagination().getPageLimit() > 100) {
            throw new ModelDBException("Page limit is invalid", Code.INVALID_ARGUMENT);
          }
        }
        Response response = repositoryDAO.listRepositories(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, ListRepositoriesRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void getRepository(
      GetRepositoryRequest request,
      StreamObserver<GetRepositoryRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        GetRepositoryRequest.Response response = repositoryDAO.getRepository(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, GetRepositoryRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void createRepository(
      SetRepository request, StreamObserver<SetRepository.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        if (request.getRepository().getName().isEmpty()) {
          throw new ModelDBException("Repository name is empty", Code.INVALID_ARGUMENT);
        }

        SetRepository.Response response = repositoryDAO.setRepository(request, true);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(responseObserver, e, SetRepository.Response.getDefaultInstance());
    }
  }

  @Override
  public void updateRepository(
      SetRepository request, StreamObserver<SetRepository.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        if (request.getRepository().getName().isEmpty()) {
          throw new ModelDBException("Repository name is empty", Code.INVALID_ARGUMENT);
        }

        SetRepository.Response response = repositoryDAO.setRepository(request, false);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(responseObserver, e, SetRepository.Response.getDefaultInstance());
    }
  }

  @Override
  public void deleteRepository(
      DeleteRepositoryRequest request,
      StreamObserver<DeleteRepositoryRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        DeleteRepositoryRequest.Response response = repositoryDAO.deleteRepository(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, DeleteRepositoryRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void listCommits(
      ListCommitsRequest request, StreamObserver<ListCommitsRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try (RequestLatencyResource latencyResource =
        new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
      ListCommitsRequest.Response response =
          commitDAO.listCommits(
              request,
              (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, ListCommitsRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void getCommit(
      GetCommitRequest request, StreamObserver<GetCommitRequest.Response> responseObserver) {

    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        Commit commit =
            commitDAO.getCommit(
                request.getCommitSha(),
                (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));
        responseObserver.onNext(GetCommitRequest.Response.newBuilder().setCommit(commit).build());
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, GetCommitRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void createCommit(
      CreateCommitRequest request, StreamObserver<CreateCommitRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try (RequestLatencyResource latencyResource =
        new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
      if (request.getBlobsCount() == 0) {
        throw new ModelDBException("Blob list should not be empty", Code.INVALID_ARGUMENT);
      }
      List<BlobContainer> blobContainers = validateBlobs(request);
      UserInfo currentLoginUserInfo = authService.getCurrentLoginUserInfo();

      CreateCommitRequest.Response response =
          commitDAO.setCommit(
              authService.getVertaIdFromUserInfo(currentLoginUserInfo),
              request.getCommit(),
              (session) ->
                  datasetComponentDAO.setBlobs(session, blobContainers, fileHasher),
              (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, CreateCommitRequest.Response.getDefaultInstance());
    }
  }

  private List<BlobContainer> validateBlobs(CreateCommitRequest request)
      throws ModelDBException {
    List<BlobContainer> blobContainers = new LinkedList<>();
    for (BlobExpanded blobExpanded : request.getBlobsList()) {
      if (blobExpanded.getLocationList().isEmpty()) {
        throw new ModelDBException("Blob path should not be empty", Code.INVALID_ARGUMENT);
      }
      final BlobContainer blobContainer = BlobContainer.create(blobExpanded);
      if (blobContainer != null) {
        blobContainer.validate();
        blobContainers.add(blobContainer);
      }
    }
    return blobContainers;
  }

  @Override
  public void deleteCommit(
      DeleteCommitRequest request, StreamObserver<DeleteCommitRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        DeleteCommitRequest.Response response =
            commitDAO.deleteCommit(
                request.getCommitSha(),
                (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ModelDBUtils.observeError(
          responseObserver, e, DeleteCommitRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void listCommitBlobs(
      ListCommitBlobsRequest request,
      StreamObserver<ListCommitBlobsRequest.Response> responseObserver) {
    super.listCommitBlobs(request, responseObserver);
  }

  @Override
  public void getCommitComponent(
      GetCommitComponentRequest request,
      StreamObserver<GetCommitComponentRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try (RequestLatencyResource latencyResource =
        new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
      if (request.getCommitSha().isEmpty()) {
        throw new ModelDBException("Commit SHA should not be empty", Code.INVALID_ARGUMENT);
      } else if (request.getLocationList().isEmpty()) {
        throw new ModelDBException("Blob location should not be empty", Code.INVALID_ARGUMENT);
      }

      GetCommitComponentRequest.Response response =
          datasetComponentDAO.getCommitComponent(
              (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()),
              request.getCommitSha(),
              request.getLocationList());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, GetCommitComponentRequest.Response.getDefaultInstance());
    }
  }

  private Builder getPathInfo(PathDatasetComponentBlob path)
      throws ModelDBException, NoSuchAlgorithmException {
    // TODO: md5
    return path.toBuilder().setSha256(generateAndValidateSha(path));
  }

  String generateAndValidateSha(PathDatasetComponentBlob path)
      throws ModelDBException, NoSuchAlgorithmException {
    String sha = path.getSha256();
    String generatedSha = fileHasher.getSha(path);
    if (!sha.isEmpty() && !sha.equals(generatedSha)) {
      throw new ModelDBException("Checksum is wrong", Code.INVALID_ARGUMENT);
    }
    return generatedSha;
  }

  @Override
  public void computeRepositoryDiff(
      ComputeRepositoryDiffRequest request,
      StreamObserver<ComputeRepositoryDiffRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try (RequestLatencyResource latencyResource =
        new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
      List<BlobDiff> blobDiffList = new ArrayList<>();

      Commit internalCommitA =
          commitDAO.getCommit(
              request.getCommitA(),
              (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));

      Commit internalCommitB =
          commitDAO.getCommit(
              request.getCommitB(),
              (session) -> repositoryDAO.getRepositoryById(session, request.getRepositoryId()));

      /*InternalFolderElementEntity internalFolderElementEntity = datasetComponentDAO.getBlob();

      internalCommitA.getFolderSha();



      PathDatasetComponentBlob pathDatasetComponentBlob = PathDatasetComponentBlob.newBuilder().setPath().setSha256().build();
      PathDatasetBlob pathDatasetBlob = PathDatasetBlob.newBuilder().addAllComponents().build();


      PathDatasetDiff pathDatasetDiff = PathDatasetDiff.newBuilder().setA().setB().setDeleted().setAdded().build();
      DatasetDiff datasetDiff = DatasetDiff.newBuilder().setPath(pathDatasetDiff).build();

      BlobDiff blobDiff = BlobDiff.newBuilder().setDataset(datasetDiff).build();


      ComputeRepositoryDiffRequest.Response response = ComputeRepositoryDiffRequest.Response.newBuilder().addAllDiffs(blobDiffList).build();
      responseObserver.onNext(response);*/
      responseObserver.onCompleted();
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, ComputeRepositoryDiffRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void listTags(
      ListTagsRequest request, StreamObserver<ListTagsRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        ListTagsRequest.Response response = repositoryDAO.listTags(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(responseObserver, e, ListTagsRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void getTag(
      GetTagRequest request, StreamObserver<GetTagRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        GetTagRequest.Response response = repositoryDAO.getTag(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(responseObserver, e, GetTagRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void setTag(
      SetTagRequest request, StreamObserver<SetTagRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        SetTagRequest.Response response = repositoryDAO.setTag(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(responseObserver, e, SetTagRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void deleteTag(
      DeleteTagRequest request, StreamObserver<DeleteTagRequest.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(modelDBAuthInterceptor.getMethodName())) {
        if (request.getTag().isEmpty()) {
          throw new ModelDBException(
              "Tag not found in the DeleteTagRequest", Code.INVALID_ARGUMENT);
        }
        DeleteTagRequest.Response response = repositoryDAO.deleteTag(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, DeleteTagRequest.Response.getDefaultInstance());
    }
  }
}
