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
  id: Option[String] = None,
  name: Option[String] = None,
  description: Option[String] = None,
  dateCreated: Option[String] = None,
  dateUpdated: Option[String] = None,
  shortName: Option[String] = None,
  readmeText: Option[String] = None,
  projectVisibility: Option[ModeldbProjectVisibility] = None,
  workspaceId: Option[String] = None,
  workspaceType: Option[WorkspaceTypeEnumWorkspaceType] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  tags: Option[List[String]] = None,
  owner: Option[String] = None,
  codeVersionSnapshot: Option[ModeldbCodeVersion] = None,
  artifacts: Option[List[ModeldbArtifact]] = None
)
