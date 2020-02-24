// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.model

import ai.verta.swagger._public.modeldb.model.JobStatusEnumJobStatus._
import ai.verta.swagger._public.modeldb.model.JobTypeEnumJobType._
import ai.verta.swagger._public.modeldb.model.ValueTypeEnumValueType._
import ai.verta.swagger._public.modeldb.model.ProtobufNullValue._

case class ModeldbJob (
  id: Option[String] = None,
  description: Option[String] = None,
  start_time: Option[String] = None,
  end_time: Option[String] = None,
  metadata: Option[List[CommonKeyValue]] = None,
  job_status: Option[JobStatusEnumJobStatus] = None,
  job_type: Option[JobTypeEnumJobType] = None,
  owner: Option[String] = None
)
