// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger._public.modeldb.model.ModeldbProjectVisibility._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbGetSummaryResponse (
  name: Option[String] = None,
  last_updated_time: Option[String] = None,
  total_experiment: Option[String] = None,
  total_experiment_runs: Option[String] = None,
  last_modified_experimentRun_summary: Option[ModeldbLastModifiedExperimentRunSummary] = None,
  metrics: Option[List[ModeldbMetricsSummary]] = None
)
