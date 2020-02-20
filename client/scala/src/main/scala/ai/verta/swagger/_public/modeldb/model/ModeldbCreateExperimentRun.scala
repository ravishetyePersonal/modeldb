// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateExperimentRun (
  id: Option[String] = None,
  projectId: Option[String] = None,
  experimentId: Option[String] = None,
  name: Option[String] = None,
  description: Option[String] = None,
  dateCreated: Option[String] = None,
  dateUpdated: Option[String] = None,
  startTime: Option[String] = None,
  endTime: Option[String] = None,
  codeVersion: Option[String] = None,
  codeVersionSnapshot: Option[ModeldbCodeVersion] = None,
  parentId: Option[String] = None,
  tags: Option[List[String]] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  hyperparameters: Option[List[CommonKeyValue]] = None,
  artifacts: Option[List[ModeldbArtifact]] = None,
  datasets: Option[List[ModeldbArtifact]] = None,
  metrics: Option[List[CommonKeyValue]] = None,
  observations: Option[List[ModeldbObservation]] = None,
  features: Option[List[ModeldbFeature]] = None
)
