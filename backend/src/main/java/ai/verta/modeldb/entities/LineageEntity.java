package ai.verta.modeldb.entities;

import ai.verta.modeldb.LineageEntry;
import ai.verta.modeldb.LineageEntryEnum.LineageEntryType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lineage")
public class LineageEntity implements Serializable {

  public LineageEntity() {}

  public LineageEntity(LineageEntry input, LineageEntry output) {
    inputExternalId = input.getExternalId();
    inputType = input.getTypeValue();
    outputExternalId = output.getExternalId();
    outputType = output.getTypeValue();
  }

  @Id
  @Column(name = "input_external_id")
  private String inputExternalId;

  @Id
  @Column(name = "input_type")
  private Integer inputType;

  @Id
  @Column(name = "output_external_id")
  private String outputExternalId;

  @Id
  @Column(name = "output_type")
  private Integer outputType;

  public String getInputExternalId() {
    return inputExternalId;
  }

  public Integer getInputType() {
    return inputType;
  }

  public String getOutputExternalId() {
    return outputExternalId;
  }

  public Integer getOutputType() {
    return outputType;
  }
}
