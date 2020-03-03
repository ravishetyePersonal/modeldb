package ai.verta.modeldb.entities.environment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "python_environment_blob")
public class PythonEnvironmentBlobEntity {

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
}

