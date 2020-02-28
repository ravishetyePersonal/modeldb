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
import ai.verta.modeldb.versioning.VersioningServiceGrpc.VersioningServiceImplBase;
import io.grpc.Status.Code;
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
        if (request.hasPagination()) {
          if (request.getPagination().getPageLimit() < 1 &&
              request.getPagination().getPageLimit() > 100) {
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
