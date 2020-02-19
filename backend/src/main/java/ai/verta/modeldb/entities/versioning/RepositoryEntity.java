package ai.verta.modeldb.entities.versioning;

import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.versioning.Repository;
import ai.verta.modeldb.versioning.SetRepository;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "repository")
public class RepositoryEntity {
  public RepositoryEntity() {}

  public RepositoryEntity(String name, WorkspaceDTO workspaceDTO) {
    this.name = name;
    this.date_created = new Date().getTime();
    this.date_updated = new Date().getTime();
    this.workspace_id = workspaceDTO.getWorkspaceId();
    this.workspace_type = workspaceDTO.getWorkspaceType().getNumber();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UNSIGNED")
  private Long id;

  @Column(name = "name", columnDefinition = "varchar", length = 50)
  private String name;

  @Column(name = "date_created")
  private Long date_created;

  @Column(name = "date_updated")
  private Long date_updated;

  @Column(name = "workspace_id")
  private String workspace_id;

  @Column(name = "workspace_type", columnDefinition = "varchar")
  private Integer workspace_type;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getDate_created() {
    return date_created;
  }

  public Long getDate_updated() {
    return date_updated;
  }

  public String getWorkspace_id() {
    return workspace_id;
  }

  public Integer getWorkspace_type() {
    return workspace_type;
  }

  public Repository toProto() {
    return Repository.newBuilder()
        .setId(this.id)
        .setName(this.name)
        .setDateCreated(this.date_created)
        .setDateUpdated(this.date_updated)
        .setWorkspaceId(this.workspace_id)
        .setWorkspaceTypeValue(this.workspace_type)
        .build();
  }

  public void update(SetRepository request) {
    this.name = request.getRepository().getName();
    this.date_updated = new Date().getTime();
  }
}
