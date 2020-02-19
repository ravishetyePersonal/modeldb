// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger._public.modeldb.model.ModeldbProjectVisibility._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbProject (
  id: Option[String],
  name: Option[String],
  description: Option[String],
  dateCreated: Option[String],
  dateUpdated: Option[String],
  shortName: Option[String],
  readmeText: Option[String],
  projectVisibility: Option[ModeldbProjectVisibility],
  workspaceId: Option[String],
  workspaceType: Option[WorkspaceTypeEnumWorkspaceType],
  attributes: Option[List[CommonKeyValue]],
  tags: Option[List[String]],
  owner: Option[String],
  codeVersionSnapshot: Option[ModeldbCodeVersion],
  artifacts: Option[List[ModeldbArtifact]]
)
