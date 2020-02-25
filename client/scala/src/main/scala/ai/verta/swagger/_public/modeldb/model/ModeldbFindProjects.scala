// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger._public.modeldb.model.ModeldbProjectVisibility._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbFindProjects (
  project_ids: Option[List[String]] = None,
  predicates: Option[List[ModeldbKeyValueQuery]] = None,
  ids_only: Option[Boolean] = None,
  workspace_name: Option[String] = None,
  page_number: Option[Integer] = None,
  page_limit: Option[Integer] = None,
  ascending: Option[Boolean] = None,
  sort_key: Option[String] = None
)
