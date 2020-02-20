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
  private final ExperimentDAO experimentDAO;
  private final ExperimentRunDAO experimentRunDAO;
  private final ModelDBAuthInterceptor modelDBAuthInterceptor;
  private final FileHasher fileHasher;

  public VersioningServiceImpl(
      AuthService authService,
      RoleService roleService,
      RepositoryDAO repositoryDAO,
      CommitDAO commitDAO,
      ExperimentDAO experimentDAO,
      ExperimentRunDAO experimentRunDAO,
      ModelDBAuthInterceptor modelDBAuthInterceptor,
      FileHasher fileHasher) {
    this.authService = authService;
    this.roleService = roleService;
    this.repositoryDAO = repositoryDAO;
    this.commitDAO = commitDAO;
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
        UserInfo userInfo = authService.getCurrentLoginUserInfo();
        WorkspaceDTO workspaceDTO =
            roleService.getWorkspaceDTOByWorkspaceName(userInfo, request.getWorkspaceName());

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
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getId());

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

  private WorkspaceDTO verifyAndGetWorkspaceDTO(RepositoryIdentification id)
      throws ModelDBException {
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
      if (named.getName().isEmpty()) {
        message = "Name should not be empty";
      }
    }

    if (message != null) {
      throw new ModelDBException(message, Code.INVALID_ARGUMENT);
    }
    return workspaceDTO;
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
    super.createCommit(request, responseObserver);
  }

  @Override
  public void deleteCommit(
      DeleteCommitRequest request, StreamObserver<DeleteCommitRequest.Response> responseObserver) {
    super.deleteCommit(request, responseObserver);
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
