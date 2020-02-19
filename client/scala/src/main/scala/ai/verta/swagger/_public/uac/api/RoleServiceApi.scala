
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.uac.model._

class RoleServiceApi(client: Client, val basePath: String = "/v1") {

  def deleteRoleAsync(body: UacDeleteRole)(implicit ec: ExecutionContext): Future[Try[UacDeleteRoleResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacDeleteRole, UacDeleteRoleResponse]("POST", basePath + s"/role/deleteRole", __query, body)
  }

  def deleteRole(body: UacDeleteRole)(implicit ec: ExecutionContext): Try[UacDeleteRoleResponse] = Await.result(deleteRoleAsync(body), Duration.Inf)


  def deleteRoleBindingAsync(body: UacDeleteRoleBinding)(implicit ec: ExecutionContext): Future[Try[UacDeleteRoleBindingResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacDeleteRoleBinding, UacDeleteRoleBindingResponse]("POST", basePath + s"/role/deleteRoleBinding", __query, body)
  }

  def deleteRoleBinding(body: UacDeleteRoleBinding)(implicit ec: ExecutionContext): Try[UacDeleteRoleBindingResponse] = Await.result(deleteRoleBindingAsync(body), Duration.Inf)


  def getBindingRoleByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[UacGetRoleBindingByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, UacGetRoleBindingByIdResponse]("GET", basePath + s"/role/getRoleBindingById", __query, body)
  }

  def getBindingRoleById(id: String)(implicit ec: ExecutionContext): Try[UacGetRoleBindingByIdResponse] = Await.result(getBindingRoleByIdAsync(id), Duration.Inf)


  def getRoleBindingByNameAsync(name: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Future[Try[UacGetRoleBindingByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "scope.org_id" -> client.toQuery(scopeOrgId),
      "scope.team_id" -> client.toQuery(scopeTeamId)
    )
    val body: Any = null
    return client.request[Any, UacGetRoleBindingByNameResponse]("GET", basePath + s"/role/getRoleBindingByName", __query, body)
  }

  def getRoleBindingByName(name: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Try[UacGetRoleBindingByNameResponse] = Await.result(getRoleBindingByNameAsync(name, scopeOrgId, scopeTeamId), Duration.Inf)


  def getRoleByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[UacGetRoleByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, UacGetRoleByIdResponse]("GET", basePath + s"/role/getRoleById", __query, body)
  }

  def getRoleById(id: String)(implicit ec: ExecutionContext): Try[UacGetRoleByIdResponse] = Await.result(getRoleByIdAsync(id), Duration.Inf)


  def getRoleByNameAsync(name: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Future[Try[UacGetRoleByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "scope.org_id" -> client.toQuery(scopeOrgId),
      "scope.team_id" -> client.toQuery(scopeTeamId)
    )
    val body: Any = null
    return client.request[Any, UacGetRoleByNameResponse]("GET", basePath + s"/role/getRoleByName", __query, body)
  }

  def getRoleByName(name: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Try[UacGetRoleByNameResponse] = Await.result(getRoleByNameAsync(name, scopeOrgId, scopeTeamId), Duration.Inf)


  def listRoleBindingsAsync(entityId: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Future[Try[UacListRoleBindingsResponse]] = {
    val __query = Map[String,String](
      "entity_id" -> client.toQuery(entityId),
      "scope.org_id" -> client.toQuery(scopeOrgId),
      "scope.team_id" -> client.toQuery(scopeTeamId)
    )
    val body: Any = null
    return client.request[Any, UacListRoleBindingsResponse]("GET", basePath + s"/role/listRoleBindings", __query, body)
  }

  def listRoleBindings(entityId: String, scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Try[UacListRoleBindingsResponse] = Await.result(listRoleBindingsAsync(entityId, scopeOrgId, scopeTeamId), Duration.Inf)


  def listRolesAsync(scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Future[Try[UacListRolesResponse]] = {
    val __query = Map[String,String](
      "scope.org_id" -> client.toQuery(scopeOrgId),
      "scope.team_id" -> client.toQuery(scopeTeamId)
    )
    val body: Any = null
    return client.request[Any, UacListRolesResponse]("GET", basePath + s"/role/listRoles", __query, body)
  }

  def listRoles(scopeOrgId: String, scopeTeamId: String)(implicit ec: ExecutionContext): Try[UacListRolesResponse] = Await.result(listRolesAsync(scopeOrgId, scopeTeamId), Duration.Inf)


  def setRoleAsync(body: UacSetRole)(implicit ec: ExecutionContext): Future[Try[UacSetRoleResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacSetRole, UacSetRoleResponse]("POST", basePath + s"/role/setRole", __query, body)
  }

  def setRole(body: UacSetRole)(implicit ec: ExecutionContext): Try[UacSetRoleResponse] = Await.result(setRoleAsync(body), Duration.Inf)


  def setRoleBindingAsync(body: UacSetRoleBinding)(implicit ec: ExecutionContext): Future[Try[UacSetRoleBindingResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacSetRoleBinding, UacSetRoleBindingResponse]("POST", basePath + s"/role/setRoleBinding", __query, body)
  }

  def setRoleBinding(body: UacSetRoleBinding)(implicit ec: ExecutionContext): Try[UacSetRoleBindingResponse] = Await.result(setRoleBindingAsync(body), Duration.Inf)

}
