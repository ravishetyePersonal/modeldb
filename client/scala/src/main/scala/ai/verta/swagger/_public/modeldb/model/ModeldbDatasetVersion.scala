// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.ArtifactTypeEnumArtifactType._
import ai.verta.swagger._public.modeldb.model.AuthzActionEnumAuthzServiceActions._
import ai.verta.swagger._public.modeldb.model.CollaboratorTypeEnumCollaboratorType._
import ai.verta.swagger._public.modeldb.model.DatasetTypeEnumDatasetType._
import ai.verta.swagger._public.modeldb.model.DatasetVisibilityEnumDatasetVisibility._
import ai.verta.swagger._public.modeldb.model.EntitiesEnumEntitiesTypes._
import ai.verta.swagger._public.modeldb.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.modeldb.model.ModelDBActionEnumModelDBServiceActions._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.PathLocationTypeEnumPathLocationType._
import ai.verta.swagger._public.modeldb.model.RoleActionEnumRoleServiceActions._
import ai.verta.swagger._public.modeldb.model.ServiceEnumService._
import ai.verta.swagger._public.modeldb.model.TernaryEnumTernary._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger._public.modeldb.model.ModeldbProjectVisibility._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._
import ai.verta.swagger._public.modeldb.model.UacFlagEnum._

case class ModeldbDatasetVersion (
  id: Option[String] = None,
  parent_id: Option[String] = None,
  dataset_id: Option[String] = None,
  time_logged: Option[String] = None,
  description: Option[String] = None,
  tags: Option[List[String]] = None,
  dataset_version_visibility: Option[DatasetVisibilityEnumDatasetVisibility] = None,
  dataset_type: Option[DatasetTypeEnumDatasetType] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  owner: Option[String] = None,
  version: Option[String] = None,
  raw_dataset_version_info: Option[ModeldbRawDatasetVersionInfo] = None,
  path_dataset_version_info: Option[ModeldbPathDatasetVersionInfo] = None,
  query_dataset_version_info: Option[ModeldbQueryDatasetVersionInfo] = None,
  time_updated: Option[String] = None
)
