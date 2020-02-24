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
  entity_ids: Option[List[String]] = None,
  share_with: Option[String] = None,
  collaborator_type: Option[CollaboratorTypeEnumCollaboratorType] = None,
  message: Option[String] = None,
  date_created: Option[String] = None,
  date_updated: Option[String] = None,
  can_deploy: Option[TernaryEnumTernary] = None,
  authz_entity_type: Option[EntitiesEnumEntitiesTypes] = None
)
