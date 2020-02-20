
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.uac.model._

class OrganizationApi(client: Client, val basePath: String = "/v1") {

  def addUserAsync(body: UacAddUser)(implicit ec: ExecutionContext): Future[Try[UacAddUserResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacAddUser, UacAddUserResponse]("POST", basePath + s"/organization/addUser", __query, body)
  }

  def addUser(body: UacAddUser)(implicit ec: ExecutionContext): Try[UacAddUserResponse] = Await.result(addUserAsync(body), Duration.Inf)


  def deleteOrganizationAsync(body: UacDeleteOrganization)(implicit ec: ExecutionContext): Future[Try[UacDeleteOrganizationResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacDeleteOrganization, UacDeleteOrganizationResponse]("POST", basePath + s"/organization/deleteOrganization", __query, body)
  }

  def deleteOrganization(body: UacDeleteOrganization)(implicit ec: ExecutionContext): Try[UacDeleteOrganizationResponse] = Await.result(deleteOrganizationAsync(body), Duration.Inf)


  def getOrganizationByIdAsync(orgId: String)(implicit ec: ExecutionContext): Future[Try[UacGetOrganizationByIdResponse]] = {
    val __query = Map[String,String](
      "org_id" -> client.toQuery(orgId)
    )
    val body: Any = null
    return client.request[Any, UacGetOrganizationByIdResponse]("GET", basePath + s"/organization/getOrganizationById", __query, body)
  }

  def getOrganizationById(orgId: String)(implicit ec: ExecutionContext): Try[UacGetOrganizationByIdResponse] = Await.result(getOrganizationByIdAsync(orgId), Duration.Inf)


  def getOrganizationByNameAsync(orgName: String)(implicit ec: ExecutionContext): Future[Try[UacGetOrganizationByNameResponse]] = {
    val __query = Map[String,String](
      "org_name" -> client.toQuery(orgName)
    )
    val body: Any = null
    return client.request[Any, UacGetOrganizationByNameResponse]("GET", basePath + s"/organization/getOrganizationByName", __query, body)
  }

  def getOrganizationByName(orgName: String)(implicit ec: ExecutionContext): Try[UacGetOrganizationByNameResponse] = Await.result(getOrganizationByNameAsync(orgName), Duration.Inf)


  def getOrganizationByShortNameAsync(shortName: String)(implicit ec: ExecutionContext): Future[Try[UacGetOrganizationByShortNameResponse]] = {
    val __query = Map[String,String](
      "short_name" -> client.toQuery(shortName)
    )
    val body: Any = null
    return client.request[Any, UacGetOrganizationByShortNameResponse]("GET", basePath + s"/organization/getOrganizationByShortName", __query, body)
  }

  def getOrganizationByShortName(shortName: String)(implicit ec: ExecutionContext): Try[UacGetOrganizationByShortNameResponse] = Await.result(getOrganizationByShortNameAsync(shortName), Duration.Inf)


  def listMyOrganizationsAsync()(implicit ec: ExecutionContext): Future[Try[UacListMyOrganizationsResponse]] = {
    val __query = Map[String,String](
      
    )
    val body: Any = null
    return client.request[Any, UacListMyOrganizationsResponse]("GET", basePath + s"/organization/listMyOrganizations", __query, body)
  }

  def listMyOrganizations()(implicit ec: ExecutionContext): Try[UacListMyOrganizationsResponse] = Await.result(listMyOrganizationsAsync(), Duration.Inf)


  def listTeamsAsync(orgId: String)(implicit ec: ExecutionContext): Future[Try[UacListTeamsResponse]] = {
    val __query = Map[String,String](
      "org_id" -> client.toQuery(orgId)
    )
    val body: Any = null
    return client.request[Any, UacListTeamsResponse]("GET", basePath + s"/organization/listTeams", __query, body)
  }

  def listTeams(orgId: String)(implicit ec: ExecutionContext): Try[UacListTeamsResponse] = Await.result(listTeamsAsync(orgId), Duration.Inf)


  def listUsersAsync(orgId: String)(implicit ec: ExecutionContext): Future[Try[UacListUsersResponse]] = {
    val __query = Map[String,String](
      "org_id" -> client.toQuery(orgId)
    )
    val body: Any = null
    return client.request[Any, UacListUsersResponse]("GET", basePath + s"/organization/listUsers", __query, body)
  }

  def listUsers(orgId: String)(implicit ec: ExecutionContext): Try[UacListUsersResponse] = Await.result(listUsersAsync(orgId), Duration.Inf)


  def removeUserAsync(body: UacRemoveUser)(implicit ec: ExecutionContext): Future[Try[UacRemoveUserResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacRemoveUser, UacRemoveUserResponse]("POST", basePath + s"/organization/removeUser", __query, body)
  }

  def removeUser(body: UacRemoveUser)(implicit ec: ExecutionContext): Try[UacRemoveUserResponse] = Await.result(removeUserAsync(body), Duration.Inf)


  def setOrganizationAsync(body: UacSetOrganization)(implicit ec: ExecutionContext): Future[Try[UacSetOrganizationResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacSetOrganization, UacSetOrganizationResponse]("POST", basePath + s"/organization/setOrganization", __query, body)
  }

  def setOrganization(body: UacSetOrganization)(implicit ec: ExecutionContext): Try[UacSetOrganizationResponse] = Await.result(setOrganizationAsync(body), Duration.Inf)

}
