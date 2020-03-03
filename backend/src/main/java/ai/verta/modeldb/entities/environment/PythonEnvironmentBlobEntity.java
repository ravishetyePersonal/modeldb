package ai.verta.modeldb.entities.environment;

import ai.verta.modeldb.entities.ComponentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "python_environment_blob")
public class PythonEnvironmentBlobEntity implements ComponentEntity {

  public PythonEnvironmentBlobEntity() {}

  @Id
  @Column(name = "blob_hash", nullable = false, columnDefinition = "varchar", length = 64)
  private String blob_hash;

  @Column(name = "major")
  private Integer major;

  @Column(name = "minor")
  private Integer minor;

  @Column(name = "patch")
  private Integer patch;

  @Override
  public void setBlobHash(String elementSha) {
    blob_hash = elementSha;
  }

  @Override
  public void setBaseBlobHash(String folderHash) {
  }
}

