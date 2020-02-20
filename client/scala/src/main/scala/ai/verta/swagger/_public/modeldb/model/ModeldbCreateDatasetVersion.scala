// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.DatasetTypeEnumDatasetType._
import ai.verta.swagger._public.modeldb.model.DatasetVisibilityEnumDatasetVisibility._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.PathLocationTypeEnumPathLocationType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateDatasetVersion (
  datasetId: Option[String],
  parentId: Option[String],
  description: Option[String],
  tags: Option[List[String]],
  datasetVersionVisibility: Option[DatasetVisibilityEnumDatasetVisibility],
  datasetType: Option[DatasetTypeEnumDatasetType],
  attributes: Option[List[CommonKeyValue]],
  version: Option[String],
  rawDatasetVersionInfo: Option[ModeldbRawDatasetVersionInfo],
  pathDatasetVersionInfo: Option[ModeldbPathDatasetVersionInfo],
  queryDatasetVersionInfo: Option[ModeldbQueryDatasetVersionInfo],
  timeCreated: Option[String]
)
