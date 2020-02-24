// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.CollaboratorTypeEnumCollaboratorType._
import ai.verta.swagger._public.uac.model.TernaryEnumTernary._

case class UacOrganization (
  id: Option[String] = None,
  name: Option[String] = None,
  short_name: Option[String] = None,
  description: Option[String] = None,
  owner_id: Option[String] = None,
  created_timestamp: Option[String] = None,
  updated_timestamp: Option[String] = None,
  global_collaborator_type: Option[CollaboratorTypeEnumCollaboratorType] = None,
  global_can_deploy: Option[TernaryEnumTernary] = None
)
