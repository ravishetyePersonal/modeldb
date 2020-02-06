package ai.verta.modeldb.lineage;

import ai.verta.modeldb.AddLineage;
import ai.verta.modeldb.DeleteLineage;
import ai.verta.modeldb.FindAllInputs;
import ai.verta.modeldb.FindAllInputsOutputs;
import ai.verta.modeldb.FindAllOutputs;
import ai.verta.modeldb.LineageServiceGrpc.LineageServiceImplBase;
import ai.verta.modeldb.ModelDBAuthInterceptor;
import ai.verta.modeldb.ModelDBConstants;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.monitoring.ErrorCountResource;
import ai.verta.modeldb.monitoring.QPSCountResource;
import ai.verta.modeldb.monitoring.RequestLatencyResource;
import com.google.protobuf.Any;
import com.google.rpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LineageServiceImpl extends LineageServiceImplBase {

  private static final Logger LOGGER = LogManager.getLogger(LineageServiceImpl.class);
  private LineageDAO lineageDAO;

  public LineageServiceImpl(LineageDAO lineageDAO) {
    this.lineageDAO = lineageDAO;
  }

  private <T> void observeError(StreamObserver<T> responseObserver, Exception e) {
    Status status;
    if (e instanceof ModelDBException) {
      LOGGER.warn("Exception occured:", e);
      ModelDBException ModelDBException = (ModelDBException) e;
      status =
          Status.newBuilder()
              .setCode(ModelDBException.getCode().value())
              .setMessage(ModelDBException.getMessage())
              .addDetails(Any.pack(AddLineage.Response.getDefaultInstance()))
              .build();
    } else {
      LOGGER.error("Exception occured:", e);
      status =
          Status.newBuilder()
              .setCode(Code.INTERNAL.value())
              .setMessage(ModelDBConstants.INTERNAL_ERROR)
              .addDetails(Any.pack(AddLineage.Response.getDefaultInstance()))
              .build();
    }
    StatusRuntimeException statusRuntimeException = StatusProto.toStatusRuntimeException(status);
    ErrorCountResource.inc(statusRuntimeException);
    responseObserver.onError(statusRuntimeException);
  }

  @Override
  public void addLineage(AddLineage request, StreamObserver<AddLineage.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      if (request.getInputCount() == 0 && request.getOutputCount() == 0) {
        throw new ModelDBException("Input and output not specified", Code.INVALID_ARGUMENT);
      } else {
        if (request.getInputCount() == 0) {
          throw new ModelDBException("Input not specified", Code.INVALID_ARGUMENT);
        } else if (request.getOutputCount() == 0) {
          throw new ModelDBException("Output not specified", Code.INVALID_ARGUMENT);
        }
      }
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(ModelDBAuthInterceptor.METHOD_NAME.get())) {
        AddLineage.Response response = lineageDAO.addLineage(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      observeError(responseObserver, e);
    }
  }

  @Override
  public void deleteLineage(
      DeleteLineage request, StreamObserver<DeleteLineage.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      if (request.getInputCount() == 0 && request.getOutputCount() == 0) {
        throw new ModelDBException("Input and output not specified", Code.INVALID_ARGUMENT);
      } else {
        if (request.getInputCount() == 0) {
          throw new ModelDBException("Input not specified", Code.INVALID_ARGUMENT);
        } else if (request.getOutputCount() == 0) {
          throw new ModelDBException("Output not specified", Code.INVALID_ARGUMENT);
        }
      }
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(ModelDBAuthInterceptor.METHOD_NAME.get())) {
        DeleteLineage.Response response = lineageDAO.deleteLineage(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      observeError(responseObserver, e);
    }
  }

  @Override
  public void findAllInputs(
      FindAllInputs request, StreamObserver<FindAllInputs.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      if (request.getItemsCount() == 0) {
        throw new ModelDBException("Items not specified", Code.INVALID_ARGUMENT);
      }
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(ModelDBAuthInterceptor.METHOD_NAME.get())) {
        FindAllInputs.Response response = lineageDAO.findAllInputs(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      observeError(responseObserver, e);
    }
  }

  @Override
  public void findAllOutputs(
      FindAllOutputs request, StreamObserver<FindAllOutputs.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      if (request.getItemsCount() == 0) {
        throw new ModelDBException("Items not specified", Code.INVALID_ARGUMENT);
      }
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(ModelDBAuthInterceptor.METHOD_NAME.get())) {
        FindAllOutputs.Response response = lineageDAO.findAllOutputs(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      observeError(responseObserver, e);
    }
  }

  @Override
  public void findAllInputsOutputs(
      FindAllInputsOutputs request,
      StreamObserver<FindAllInputsOutputs.Response> responseObserver) {
    QPSCountResource.inc();
    try {
      if (request.getItemsCount() == 0) {
        throw new ModelDBException("Items not specified", Code.INVALID_ARGUMENT);
      }
      try (RequestLatencyResource latencyResource =
          new RequestLatencyResource(ModelDBAuthInterceptor.METHOD_NAME.get())) {
        FindAllInputsOutputs.Response response = lineageDAO.findAllInputsOutputs(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      observeError(responseObserver, e);
    }
  }
}
