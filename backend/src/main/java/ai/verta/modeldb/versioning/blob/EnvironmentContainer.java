package ai.verta.modeldb.versioning.blob;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.environment.EnvironmentBlobEntity;
import ai.verta.modeldb.entities.environment.PythonEnvironmentBlobEntity;
import ai.verta.modeldb.entities.environment.PythonEnvironmentRequirementBlobEntity;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DockerEnvironmentBlob;
import ai.verta.modeldb.versioning.EnvironmentBlob;
import ai.verta.modeldb.versioning.EnvironmentVariablesBlob;
import ai.verta.modeldb.versioning.FileHasher;
import ai.verta.modeldb.versioning.PythonEnvironmentBlob;
import ai.verta.modeldb.versioning.PythonRequirementEnvironmentBlob;
import ai.verta.modeldb.versioning.TreeElem;
import ai.verta.modeldb.versioning.VersionEnvironmentBlob;
import io.grpc.Status.Code;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.hibernate.Session;

public class EnvironmentContainer extends BlobContainer {

  private static final Integer PYTHON_ENV_TYPE = 1;
  private final EnvironmentBlob environment;

  public EnvironmentContainer(BlobExpanded blobExpanded) {
    super(blobExpanded);
    environment = blobExpanded.getBlob().getEnvironment();
  }

  @Override
  public void validate() throws ModelDBException {
    for (EnvironmentVariablesBlob blob : environment.getEnvironmentVariablesList()) {
      validateEnvironmentVariableName(blob.getName());
    }
    switch (environment.getContentCase()) {
      case DOCKER:
        if (environment.getDocker().getRepository().isEmpty()) {
          throw new ModelDBException("Environment repository path should not be empty",
              Code.INVALID_ARGUMENT);
        }
        break;
      case PYTHON:
        final PythonEnvironmentBlob python = environment.getPython();
        for (PythonRequirementEnvironmentBlob requirement : python
            .getRequirementsList()) {
          if (requirement.getLibrary().isEmpty()) {
            throw new ModelDBException("Requirement library name should not be empty");
          }
        }
        for (PythonRequirementEnvironmentBlob constraint : python
            .getConstraintsList()) {
          if (constraint.getConstraint().isEmpty()) {
            throw new ModelDBException("Constraint  name should not be empty");
          }
        }
        break;
      default:
        throw new ModelDBException("Blob unknown type", Code.INVALID_ARGUMENT);
    }
  }

  @Override
  public void process(Session session, TreeElem rootTree, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException {
    EnvironmentBlobEntity environmentBlobEntity = new EnvironmentBlobEntity();
    String blobHash = computeSHA(environment);

    String blobType;
    List<Object> entities = new LinkedList<>();
    entities.add(environmentBlobEntity);
    switch (environment.getContentCase()) {
      case PYTHON:
        blobType = PythonEnvironmentBlob.class.getSimpleName();
        PythonEnvironmentBlob python = environment.getPython();
        final String pythonBlobHash = computeSHA(python);
        environmentBlobEntity.setBlob_hash(FileHasher.getSha((blobHash + pythonBlobHash)));
        environmentBlobEntity.setEnvironment_type(PYTHON_ENV_TYPE);
        PythonEnvironmentBlobEntity pythonEnvironmentBlobEntity = new PythonEnvironmentBlobEntity();
        environmentBlobEntity.setPythonEnvironmentBlobEntity(pythonEnvironmentBlobEntity);
        pythonEnvironmentBlobEntity.setBlob_hash(pythonBlobHash);
        final VersionEnvironmentBlob version = python.getVersion();
        pythonEnvironmentBlobEntity.setMajor(version.getMajor());
        pythonEnvironmentBlobEntity.setMinor(version.getMinor());
        pythonEnvironmentBlobEntity.setPatch(version.getPatch());
        entities.add(pythonEnvironmentBlobEntity);
        for (PythonRequirementEnvironmentBlob pythonRequirementEnvironmentBlob : python
            .getRequirementsList()) {
          PythonEnvironmentRequirementBlobEntity pythonEnvironmentRequirementBlobEntity =
              new PythonEnvironmentRequirementBlobEntity(pythonRequirementEnvironmentBlob,
                  pythonEnvironmentBlobEntity, true);
          entities.add(pythonEnvironmentRequirementBlobEntity);
        }
        for (PythonRequirementEnvironmentBlob pythonRequirementEnvironmentBlob : python
            .getConstraintsList()) {
          PythonEnvironmentRequirementBlobEntity pythonEnvironmentRequirementBlobEntity =
              new PythonEnvironmentRequirementBlobEntity(pythonRequirementEnvironmentBlob,
                  pythonEnvironmentBlobEntity, false);
          entities.add(pythonEnvironmentRequirementBlobEntity);
        }
        break;
      case DOCKER:
        blobType = DockerEnvironmentBlob.class.getSimpleName();
        break;
      default:
        throw new ModelDBException("Blob unknown type", Code.INTERNAL);
    }
    //environment.getEnvironmentVariablesList()
    for (Object entity: entities) {
      session.saveOrUpdate(entity);
    }
    rootTree.push(
        getLocationList(),
        fileHasher.getSha(environment),
        blobType,
        null);
  }

  private static final String PATTERN =
      "[a-zA-Z0-9_-]+";
  private void validateEnvironmentVariableName(String name) throws ModelDBException {
    if (!Pattern.compile(PATTERN).matcher(name).matches()) {
      throw new ModelDBException(
          "Environment variable name: " + name
              + " should be not empty, should contain only alphanumeric or underscore",
          Code.INVALID_ARGUMENT);
    }
  }

  private String computeSHA(EnvironmentBlob blob) throws NoSuchAlgorithmException {
    StringBuilder sb = new StringBuilder();
    sb.append("env:");
    for (EnvironmentVariablesBlob environmentVariablesBlob : blob.getEnvironmentVariablesList()) {
      sb.append("name:").append(environmentVariablesBlob.getName()).append("value:")
          .append(environmentVariablesBlob.getValue());
    }
    sb.append("command_line:");
    for (String commandLine : blob.getCommandLineList()) {
      sb.append("command:").append(commandLine);
    }
    return FileHasher.getSha(sb.toString());
  }

  private String computeSHA(PythonEnvironmentBlob blob) throws NoSuchAlgorithmException {
    StringBuilder sb = new StringBuilder();
    final VersionEnvironmentBlob version = blob.getVersion();
    sb.append("python:");
    appendVersion(sb, version);
    sb.append("requirements:");
    for (PythonRequirementEnvironmentBlob pythonRequirementEnvironmentBlob : blob
        .getRequirementsList()) {
      sb.append("library:").append(pythonRequirementEnvironmentBlob.getLibrary())
          .append("constraint:")
          .append(pythonRequirementEnvironmentBlob.getConstraint());
      appendVersion(sb, pythonRequirementEnvironmentBlob.getVersion());
    }
    sb.append("constraints:");
    for (PythonRequirementEnvironmentBlob pythonConstraintEnvironmentBlob : blob
        .getConstraintsList()) {
      sb.append("library:").append(pythonConstraintEnvironmentBlob.getLibrary())
          .append("constraint:")
          .append(pythonConstraintEnvironmentBlob.getConstraint());
      appendVersion(sb, pythonConstraintEnvironmentBlob.getVersion());
    }
    return FileHasher.getSha(sb.toString());
  }

  private String computeSHA(DockerEnvironmentBlob blob) throws NoSuchAlgorithmException {
    StringBuilder sb = new StringBuilder();
    sb.append("docker:repository").append(blob.getRepository()).append("sha:")
        .append(blob.getSha()).append("tag:").append(blob.getTag());
    return FileHasher.getSha(sb.toString());

  }

  private void appendVersion(StringBuilder sb, VersionEnvironmentBlob version) {
    sb.append("version:").append("major:").append(version.getMajor()).append("minor")
        .append(version.getMinor()).append("patch").append(version.getPatch());
  }
}
