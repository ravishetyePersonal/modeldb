package ai.verta.modeldb.entities.environment;

import ai.verta.modeldb.entities.ComponentEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "environment_blob")
public class EnvironmentBlobEntity implements ComponentEntity {

  public EnvironmentBlobEntity() {}

  @Id
  @Column(name = "blob_hash", nullable = false, columnDefinition = "varchar", length = 64)
  private String blob_hash;

  @Column(name = "environment_type")
  private Integer environment_type;

  @OneToOne(targetEntity = PythonEnvironmentBlobEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "python_environment_blob_hash")
  private PythonEnvironmentBlobEntity pythonEnvironmentBlobEntity;

  @OneToOne(targetEntity = DockerEnvironmentBlobEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "docker_environment_blob_hash")
  private DockerEnvironmentBlobEntity dockerEnvironmentBlobEntity;

  @Override
  public void setBlobHash(String elementSha) {
    blob_hash = elementSha;
  }

  @Override
  public void setBaseBlobHash(String folderHash) {
  }
}

