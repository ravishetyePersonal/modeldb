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
@Table(name = "environment_variables")
public class EnvironmentVariablesEntity implements Serializable {
  public EnvironmentVariablesEntity() {}

  @Id
  @OneToOne(targetEntity = EnvironmentBlobEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "environment_blob_hash")
  private EnvironmentBlobEntity environmentBlobEntity;

  @Id
  @Column(name = "variable_name")
  private String variable_name;

  @Column(name = "variable_value")
  private String variable_value;
}

