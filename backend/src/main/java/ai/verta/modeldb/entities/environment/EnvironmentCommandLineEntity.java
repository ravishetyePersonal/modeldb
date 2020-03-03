package ai.verta.modeldb.entities.environment;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "environment_command_line")
public class EnvironmentCommandLineEntity implements Serializable {
  public EnvironmentCommandLineEntity() {}

  @Id
  @OneToOne(targetEntity = EnvironmentBlobEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "environment_blob_hash")
  private EnvironmentBlobEntity environmentBlobEntity;

  @Id
  @Column(name = "command_seq_number")
  private Integer command_seq_number;

  @Column(name = "command", columnDefinition = "TEXT")
  private String command;
}

