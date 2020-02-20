// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.DatasetTypeEnumDatasetType._
import ai.verta.swagger._public.modeldb.model.DatasetVisibilityEnumDatasetVisibility._
import ai.verta.swagger._public.modeldb.model.OperatorEnumOperator._
import ai.verta.swagger._public.modeldb.model.PathLocationTypeEnumPathLocationType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbCreateDatasetVersion (
  datasetId: Option[String] = None,
  parentId: Option[String] = None,
  description: Option[String] = None,
  tags: Option[List[String]] = None,
  datasetVersionVisibility: Option[DatasetVisibilityEnumDatasetVisibility] = None,
  datasetType: Option[DatasetTypeEnumDatasetType] = None,
  attributes: Option[List[CommonKeyValue]] = None,
  version: Option[String] = None,
  rawDatasetVersionInfo: Option[ModeldbRawDatasetVersionInfo] = None,
  pathDatasetVersionInfo: Option[ModeldbPathDatasetVersionInfo] = None,
  queryDatasetVersionInfo: Option[ModeldbQueryDatasetVersionInfo] = None,
  timeCreated: Option[String] = None
)
