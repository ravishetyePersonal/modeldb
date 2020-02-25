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

case class UacAddCollaboratorRequestResponse (
  self_allowed_actions: Option[List[UacAction]] = None,
  status: Option[Boolean] = None,
  collaborator_user_info: Option[UacUserInfo] = None,
  collaborator_organization: Option[UacOrganization] = None,
  collaborator_team: Option[UacTeam] = None
)
