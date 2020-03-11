package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.BlobDiff;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DockerEnvironmentDiff;
import ai.verta.modeldb.versioning.EnvironmentBlob;
import ai.verta.modeldb.versioning.EnvironmentDiff;
import ai.verta.modeldb.versioning.PythonEnvironmentDiff;

public class EnvironmentBlobDiffFactory extends BlobDiffFactory {

  public EnvironmentBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean subtypeEqual(BlobDiffFactory blobDiffFactory) {
    return blobDiffFactory
        .getBlobExpanded()
        .getBlob()
        .getEnvironment()
        .getContentCase()
        .equals(getBlobExpanded().getBlob().getEnvironment().getContentCase());
  }

  @Override
  protected void add(BlobDiff.Builder blobBuilder) {
    modify(blobBuilder, true);
  }

  @Override
  protected void delete(BlobDiff.Builder blobBuilder) {
    modify(blobBuilder, false);
  }

  private void modify(BlobDiff.Builder blobBuilder, boolean add) {
    final EnvironmentDiff.Builder environmentBuilder = EnvironmentDiff.newBuilder();
    final EnvironmentBlob environment = getBlobExpanded().getBlob().getEnvironment();
    switch (environment.getContentCase()) {
      case PYTHON:
        PythonEnvironmentDiff.Builder pythonBuilder;
        if (blobBuilder.hasEnvironment()) {
          pythonBuilder = blobBuilder.getEnvironment().getPython().toBuilder();
        } else {
          pythonBuilder = PythonEnvironmentDiff.newBuilder();
        }
        if (add) {
          pythonBuilder.setA(environment.getPython());
        } else {
          pythonBuilder.setB(environment.getPython());
        }

        environmentBuilder.setPython(pythonBuilder).build();
        break;
      case DOCKER:
        DockerEnvironmentDiff.Builder dockerBuilder;
        if (blobBuilder.hasEnvironment()) {
          dockerBuilder = blobBuilder.getEnvironment().getDocker().toBuilder();
        } else {
          dockerBuilder = DockerEnvironmentDiff.newBuilder();
        }
        if (add) {
          dockerBuilder.setA(environment.getDocker());
        } else {
          dockerBuilder.setB(environment.getDocker());
        }

        environmentBuilder.setDocker(dockerBuilder).build();
        break;
    }
    blobBuilder.setEnvironment(environmentBuilder.build());
  }
}
