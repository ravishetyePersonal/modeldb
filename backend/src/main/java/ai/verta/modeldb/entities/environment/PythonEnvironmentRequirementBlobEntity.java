package ai.verta.modeldb.entities.environment;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "python_environment_requirements_blob")
public class PythonEnvironmentRequirementBlobEntity implements Serializable {

  public PythonEnvironmentRequirementBlobEntity() {}

  @OneToOne(targetEntity = PythonEnvironmentBlobEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "python_environment_blob_hash")
  private PythonEnvironmentBlobEntity pythonEnvironmentBlobEntity;

  @Id
  @Column(name = "library")
  private String library;

  @Id
  @Column(name = "constraint")
  private String constraint;

  @Column(name = "major")
  private Integer major;

  @Column(name = "minor")
  private Integer minor;

  @Column(name = "patch")
  private Integer patch;

  @Id
  @Column(name = "req_or_constraint", nullable = false)
  private Boolean req_or_constraint;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PythonEnvironmentRequirementBlobEntity that = (PythonEnvironmentRequirementBlobEntity) o;
    return Objects.equals(pythonEnvironmentBlobEntity, that.pythonEnvironmentBlobEntity) &&
        Objects.equals(library, that.library) &&
        Objects.equals(constraint, that.constraint) &&
        Objects.equals(major, that.major) &&
        Objects.equals(minor, that.minor) &&
        Objects.equals(patch, that.patch) &&
        Objects.equals(req_or_constraint, that.req_or_constraint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pythonEnvironmentBlobEntity, library, constraint, major, minor, patch,
        req_or_constraint);
  }
}

