// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.AuthzActionEnumAuthzServiceActions._
import ai.verta.swagger._public.uac.model.AuthzResourceEnumAuthzServiceResourceTypes._
import ai.verta.swagger._public.uac.model.ModelDBActionEnumModelDBServiceActions._
import ai.verta.swagger._public.uac.model.ModelResourceEnumModelDBServiceResourceTypes._
import ai.verta.swagger._public.uac.model.RoleActionEnumRoleServiceActions._
import ai.verta.swagger._public.uac.model.RoleResourceEnumRoleServiceResourceTypes._
import ai.verta.swagger._public.uac.model.ServiceEnumService._

case class UacResources (
  service: Option[ServiceEnumService] = None,
  resource_ids: Option[List[String]] = None,
  role_service_resource_type: Option[RoleResourceEnumRoleServiceResourceTypes] = None,
  authz_service_resource_type: Option[AuthzResourceEnumAuthzServiceResourceTypes] = None,
  modeldb_service_resource_type: Option[ModelResourceEnumModelDBServiceResourceTypes] = None
)
