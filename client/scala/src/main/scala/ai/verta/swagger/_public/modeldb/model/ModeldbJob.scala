// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.JobStatusEnumJobStatus._
import ai.verta.swagger._public.modeldb.model.JobTypeEnumJobType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbJob (
  id: Option[String],
  description: Option[String],
  startTime: Option[String],
  endTime: Option[String],
  metadata: Option[List[CommonKeyValue]],
  jobStatus: Option[JobStatusEnumJobStatus],
  jobType: Option[JobTypeEnumJobType],
  owner: Option[String]
)
