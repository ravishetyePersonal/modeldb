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
  projectIds: Option[List[String]],
  predicates: Option[List[ModeldbKeyValueQuery]],
  idsOnly: Option[Boolean],
  workspaceName: Option[String],
  pageNumber: Option[Integer],
  pageLimit: Option[Integer],
  ascending: Option[Boolean],
  sortKey: Option[String]
)
