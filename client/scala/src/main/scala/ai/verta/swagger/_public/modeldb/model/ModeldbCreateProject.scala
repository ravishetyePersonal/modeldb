// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger._public.modeldb.model.ModeldbProjectVisibility._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateProject (
  name: Option[String],
  description: Option[String],
  attributes: Option[List[CommonKeyValue]],
  tags: Option[List[String]],
  readmeText: Option[String],
  projectVisibility: Option[ModeldbProjectVisibility],
  artifacts: Option[List[ModeldbArtifact]],
  workspaceName: Option[String],
  dateCreated: Option[String]
)
