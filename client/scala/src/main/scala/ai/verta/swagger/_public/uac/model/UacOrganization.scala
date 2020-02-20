// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.CollaboratorTypeEnumCollaboratorType._
import ai.verta.swagger._public.uac.model.TernaryEnumTernary._

case class UacOrganization (
  id: Option[String] = None,
  name: Option[String] = None,
  shortName: Option[String] = None,
  description: Option[String] = None,
  ownerId: Option[String] = None,
  createdTimestamp: Option[String] = None,
  updatedTimestamp: Option[String] = None,
  globalCollaboratorType: Option[CollaboratorTypeEnumCollaboratorType] = None,
  globalCanDeploy: Option[TernaryEnumTernary] = None
)
