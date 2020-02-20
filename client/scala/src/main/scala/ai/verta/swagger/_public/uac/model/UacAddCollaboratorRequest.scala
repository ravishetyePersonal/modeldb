// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.AuthzActionEnumAuthzServiceActions._
import ai.verta.swagger._public.uac.model.CollaboratorTypeEnumCollaboratorType._
import ai.verta.swagger._public.uac.model.EntitiesEnumEntitiesTypes._
import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.ModelDBActionEnumModelDBServiceActions._
import ai.verta.swagger._public.uac.model.RoleActionEnumRoleServiceActions._
import ai.verta.swagger._public.uac.model.ServiceEnumService._
import ai.verta.swagger._public.uac.model.TernaryEnumTernary._
import ai.verta.swagger._public.uac.model.UacFlagEnum._
import ai.verta.swagger._public.uac.model.UacShareViaEnum._

case class UacAddCollaboratorRequest (
  entityIds: Option[List[String]] = None,
  shareWith: Option[String] = None,
  collaboratorType: Option[CollaboratorTypeEnumCollaboratorType] = None,
  message: Option[String] = None,
  dateCreated: Option[String] = None,
  dateUpdated: Option[String] = None,
  canDeploy: Option[TernaryEnumTernary] = None,
  authzEntityType: Option[EntitiesEnumEntitiesTypes] = None
)
