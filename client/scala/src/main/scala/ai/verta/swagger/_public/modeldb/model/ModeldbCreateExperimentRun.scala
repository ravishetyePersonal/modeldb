// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateExperimentRun (
  id: Option[String] = None,
  project_id: Option[String] = None,
  experiment_id: Option[String] = None,
  name: Option[String] = None,
  description: Option[String] = None,
  date_created: Option[String] = None,
  date_updated: Option[String] = None,
  start_time: Option[String] = None,
  end_time: Option[String] = None,
  code_version: Option[String] = None,
  code_version_snapshot: Option[ModeldbCodeVersion] = None,
  parent_id: Option[String] = None,
  tags: Option[List[String]] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  hyperparameters: Option[List[CommonKeyValue]] = None,
  artifacts: Option[List[ModeldbArtifact]] = None,
  datasets: Option[List[ModeldbArtifact]] = None,
  metrics: Option[List[CommonKeyValue]] = None,
  observations: Option[List[ModeldbObservation]] = None,
  features: Option[List[ModeldbFeature]] = None
)
