// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.DatasetTypeEnumDatasetType._
import ai.verta.swagger._public.modeldb.model.DatasetVisibilityEnumDatasetVisibility._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.PathLocationTypeEnumPathLocationType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateDatasetVersion (
  dataset_id: Option[String] = None,
  parent_id: Option[String] = None,
  description: Option[String] = None,
  tags: Option[List[String]] = None,
  dataset_version_visibility: Option[DatasetVisibilityEnumDatasetVisibility] = None,
  dataset_type: Option[DatasetTypeEnumDatasetType] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  version: Option[String] = None,
  raw_dataset_version_info: Option[ModeldbRawDatasetVersionInfo] = None,
  path_dataset_version_info: Option[ModeldbPathDatasetVersionInfo] = None,
  query_dataset_version_info: Option[ModeldbQueryDatasetVersionInfo] = None,
  time_created: Option[String] = None
)
