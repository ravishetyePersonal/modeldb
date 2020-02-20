// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.JobStatusEnumJobStatus._
import ai.verta.swagger._public.modeldb.model.JobTypeEnumJobType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbJob (
  id: Option[String] = None,
  description: Option[String] = None,
  startTime: Option[String] = None,
  endTime: Option[String] = None,
  metadata: Option[List[CommonKeyValue]] = None,
  jobStatus: Option[JobStatusEnumJobStatus] = None,
  jobType: Option[JobTypeEnumJobType] = None,
  owner: Option[String] = None
)
