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

  public String getBlob_hash() {
    return blob_hash;
  }

  public void setBlob_hash(String blob_hash) {
    this.blob_hash = blob_hash;
  }

  public Integer getMajor() {
    return major;
  }

  public void setMajor(Integer major) {
    this.major = major;
  }

  public Integer getMinor() {
    return minor;
  }

  public void setMinor(Integer minor) {
    this.minor = minor;
  }

  public Integer getPatch() {
    return patch;
  }

  public void setPatch(Integer patch) {
    this.patch = patch;
  }
}

