package ai.verta.modeldb.entities.environment;

import ai.verta.modeldb.entities.ComponentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "docker_environment_blob")
public class DockerEnvironmentBlobEntity implements ComponentEntity {

  public DockerEnvironmentBlobEntity() {}

  @Id
  @Column(name = "blob_hash", nullable = false, columnDefinition = "varchar", length = 64)
  private String blob_hash;

  @Column(name = "repository")
  private String repository;

  @Column(name = "tag")
  private String tag;

  @Column(name = "sha")
  private String sha;

  @Override
  public void setBlobHash(String elementSha) {
    blob_hash = elementSha;
  }

  @Override
  public void setBaseBlobHash(String folderHash) {
  }
}

