package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBAuthInterceptor;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.authservice.RoleService;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.experiment.ExperimentDAO;
import ai.verta.modeldb.experimentRun.ExperimentRunDAO;
import ai.verta.modeldb.monitoring.QPSCountResource;
import ai.verta.modeldb.monitoring.RequestLatencyResource;
import ai.verta.modeldb.utils.ModelDBUtils;
import ai.verta.modeldb.versioning.ListRepositoriesRequest.Response;
import ai.verta.modeldb.versioning.VersioningServiceGrpc.VersioningServiceImplBase;
import ai.verta.uac.UserInfo;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VersioningServiceImpl extends VersioningServiceImplBase {

  private static final Logger LOGGER = LogManager.getLogger(VersioningServiceImpl.class);
  private final AuthService authService;
  private final RoleService roleService;
  private final RepositoryDAO repositoryDAO;
  private final CommitDAO commitDAO;
  private final BlobDAO blobDAO;
  private final ExperimentDAO experimentDAO;
  private final ExperimentRunDAO experimentRunDAO;
  private final ModelDBAuthInterceptor modelDBAuthInterceptor;
  private final FileHasher fileHasher;

  public VersioningServiceImpl(
      AuthService authService,
      RoleService roleService,
      RepositoryDAO repositoryDAO,
      CommitDAO commitDAO,
      BlobDAO blobDAO,
      ExperimentDAO experimentDAO,
      ExperimentRunDAO experimentRunDAO,
      ModelDBAuthInterceptor modelDBAuthInterceptor,
      FileHasher fileHasher) {
    this.authService = authService;
    this.roleService = roleService;
    this.repositoryDAO = repositoryDAO;
    this.commitDAO = commitDAO;
    this.blobDAO = blobDAO;
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
        WorkspaceDTO workspaceDTO =
            verifyAndGetWorkspaceDTO(RepositoryIdentification.newBuilder().setNamedId(
                RepositoryNamedIdentification.newBuilder()
                    .setWorkspaceName(request.getWorkspaceName())).build(), false);

        Response response = repositoryDAO.listRepositories(request, workspaceDTO);
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
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getId());

        GetRepositoryRequest.Response response = repositoryDAO.getRepository(request, workspaceDTO);
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
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getId(), false);

        SetRepository.Response response = repositoryDAO.setRepository(request, workspaceDTO, true);
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
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getId());

        SetRepository.Response response = repositoryDAO.setRepository(request, workspaceDTO, false);
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
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getRepositoryId());
        DeleteRepositoryRequest.Response response =
            repositoryDAO.deleteRepository(request, workspaceDTO);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      ModelDBUtils.observeError(
          responseObserver, e, DeleteRepositoryRequest.Response.getDefaultInstance());
    }
  }

  private WorkspaceDTO verifyAndGetWorkspaceDTO(
      RepositoryIdentification id, boolean shouldCheckNamed) throws ModelDBException {
    WorkspaceDTO workspaceDTO = null;
    String message = null;
    if (id.hasNamedId()) {
      UserInfo userInfo;
      try {
        userInfo = authService.getCurrentLoginUserInfo();
      } catch (StatusRuntimeException e) {
        throw new ModelDBException("Authorization error", e.getStatus().getCode());
      }
      RepositoryNamedIdentification named = id.getNamedId();
      try {
        workspaceDTO =
            roleService.getWorkspaceDTOByWorkspaceName(userInfo, named.getWorkspaceName());
      } catch (StatusRuntimeException e) {
        throw new ModelDBException("Error getting workspace", e.getStatus().getCode());
      }
      if (named.getName().isEmpty() && shouldCheckNamed) {
        message = "Name should not be empty";
      }
    }

    if (message != null) {
      throw new ModelDBException(message, Code.INVALID_ARGUMENT);
    }
    return workspaceDTO;
  }

  private WorkspaceDTO verifyAndGetWorkspaceDTO(RepositoryIdentification id)
      throws ModelDBException {
    return verifyAndGetWorkspaceDTO(id, true);
  }

  @Override
  public void listCommits(
      ListCommitsRequest request, StreamObserver<ListCommitsRequest.Response> responseObserver) {
    super.listCommits(request, responseObserver);
  }

  @Override
  public void getCommit(
      GetCommitRequest request, StreamObserver<GetCommitRequest.Response> responseObserver) {
    super.getCommit(request, responseObserver);
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
      WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getRepositoryId());
      CreateCommitRequest.Builder newRequest = CreateCommitRequest.newBuilder();
      for (BlobExpanded blob : request.getBlobsList()) {
        if (blob.getPath().isEmpty()) {
          throw new ModelDBException("Blob path should not be empty", Code.INVALID_ARGUMENT);
        }
        final DatasetBlob dataset = blob.getBlob().getDataset();
        final DatasetBlob.Builder newDataset = DatasetBlob.newBuilder();

        switch (dataset.getContentCase()) {
          case S3:
            S3DatasetBlob.Builder newS3BlobBuilder = S3DatasetBlob.newBuilder();
            for (S3DatasetComponentBlob component : dataset.getS3().getComponentsList()) {
              if (!component.hasPath()) {
                throw new ModelDBException("Blob path should not be empty",
                    Code.INVALID_ARGUMENT);
              }
              newS3BlobBuilder.addComponents(component.toBuilder().setPath(component.getPath()
                  .toBuilder().setSha256(generateAndValidateSha(component.getPath()))));
            }
            newDataset.setS3(newS3BlobBuilder);
            break;
          case PATH:
            PathDatasetBlob.Builder newPathBlobBuilder = PathDatasetBlob.newBuilder();
            for (PathDatasetComponentBlob component : dataset.getPath().getComponentsList()) {
              newPathBlobBuilder.addComponents((component.toBuilder()
                  .setSha256(generateAndValidateSha(component))));
            }
            newDataset.setPath(newPathBlobBuilder);
            break;
          default:
            throw new ModelDBException("Blob unknown type", Code.INVALID_ARGUMENT);
        }
        newRequest.addBlobs(blob.toBuilder().setBlob(Blob.newBuilder().setDataset(newDataset)));
      }

      CreateCommitRequest.Response response = commitDAO.setCommit(request.getCommit(),
          (session) -> blobDAO.setBlobs(session, request.getBlobsList()),
          (session) ->
              repositoryDAO.getRepositoryById(session, request.getRepositoryId(), workspaceDTO));

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ModelDBUtils
          .observeError(responseObserver, e, CreateCommitRequest.Response.getDefaultInstance());
    }
  }

  @Override
  public void listCommitBlobs(
      ListCommitBlobsRequest request,
      StreamObserver<ListCommitBlobsRequest.Response> responseObserver) {
    super.listCommitBlobs(request, responseObserver);
  }

  @Override
  public void getCommitBlob(
      GetCommitBlobRequest request,
      StreamObserver<GetCommitBlobRequest.Response> responseObserver) {
    super.getCommitBlob(request, responseObserver);
  }

  @Override
  public void getCommitFolder(
      GetCommitFolderRequest request,
      StreamObserver<GetCommitFolderRequest.Response> responseObserver) {
    super.getCommitFolder(request, responseObserver);
  }

  String generateAndValidateSha(PathDatasetComponentBlob path) throws ModelDBException {
    String sha = path.getSha256();
    String generatedSha = fileHasher.generateSha(path.getPath(), false);
    if (!sha.isEmpty() && !sha.equals(generatedSha)) {
      throw new ModelDBException("Checksum is wrong", Code.INVALID_ARGUMENT);
    }
    return generatedSha;
  }

  @Override
  public void computeRepositoryDiff(
      ComputeRepositoryDiffRequest request,
      StreamObserver<ComputeRepositoryDiffRequest.Response> responseObserver) {
    super.computeRepositoryDiff(request, responseObserver);
  }

  @Override
  public void listTags(
      ListTagsRequest request, StreamObserver<ListTagsRequest.Response> responseObserver) {
    super.listTags(request, responseObserver);
  }

  @Override
  public void getTag(
      GetTagRequest request, StreamObserver<GetTagRequest.Response> responseObserver) {
    super.getTag(request, responseObserver);
  }

  @Override
  public void setTag(
      SetTagRequest request, StreamObserver<SetTagRequest.Response> responseObserver) {
    super.setTag(request, responseObserver);
  }

  @Override
  public void deleteTag(
      DeleteTagRequest request, StreamObserver<DeleteTagRequest.Response> responseObserver) {
    super.deleteTag(request, responseObserver);
  }
}
