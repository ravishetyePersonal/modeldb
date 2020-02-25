// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.HttpClient
import ai.verta.swagger._public.uac.model._

class TeamApi(client: HttpClient, val basePath: String = "/v1") {
  def addUserAsync(body: UacAddTeamUser)(implicit ec: ExecutionContext): Future[Try[UacAddTeamUserResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacAddTeamUser, UacAddTeamUserResponse]("POST", basePath + s"/team/addUser", __query, body)
  }

  def addUser(body: UacAddTeamUser)(implicit ec: ExecutionContext): Try[UacAddTeamUserResponse] = Await.result(addUserAsync(body), Duration.Inf)

  def deleteTeamAsync(body: UacDeleteTeam)(implicit ec: ExecutionContext): Future[Try[UacDeleteTeamResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacDeleteTeam, UacDeleteTeamResponse]("POST", basePath + s"/team/deleteTeam", __query, body)
  }

  def deleteTeam(body: UacDeleteTeam)(implicit ec: ExecutionContext): Try[UacDeleteTeamResponse] = Await.result(deleteTeamAsync(body), Duration.Inf)

  def getTeamByIdAsync(team_id: String)(implicit ec: ExecutionContext): Future[Try[UacGetTeamByIdResponse]] = {
    val __query = Map[String,String](
      "team_id" -> client.toQuery(team_id)
    )
    val body: Any = null
    return client.request[Any, UacGetTeamByIdResponse]("GET", basePath + s"/team/getTeamById", __query, body)
  }

  def getTeamById(team_id: String)(implicit ec: ExecutionContext): Try[UacGetTeamByIdResponse] = Await.result(getTeamByIdAsync(team_id), Duration.Inf)

  def getTeamByNameAsync(org_id: String, team_name: String)(implicit ec: ExecutionContext): Future[Try[UacGetTeamByNameResponse]] = {
    val __query = Map[String,String](
      "org_id" -> client.toQuery(org_id),
      "team_name" -> client.toQuery(team_name)
    )
    val body: Any = null
    return client.request[Any, UacGetTeamByNameResponse]("GET", basePath + s"/team/getTeamByName", __query, body)
  }

  def getTeamByName(org_id: String, team_name: String)(implicit ec: ExecutionContext): Try[UacGetTeamByNameResponse] = Await.result(getTeamByNameAsync(org_id, team_name), Duration.Inf)

  def getTeamByShortNameAsync(org_id: String, short_name: String)(implicit ec: ExecutionContext): Future[Try[UacGetTeamByShortNameResponse]] = {
    val __query = Map[String,String](
      "org_id" -> client.toQuery(org_id),
      "short_name" -> client.toQuery(short_name)
    )
    val body: Any = null
    return client.request[Any, UacGetTeamByShortNameResponse]("GET", basePath + s"/team/getTeamByShortName", __query, body)
  }

  def getTeamByShortName(org_id: String, short_name: String)(implicit ec: ExecutionContext): Try[UacGetTeamByShortNameResponse] = Await.result(getTeamByShortNameAsync(org_id, short_name), Duration.Inf)

  def listMyTeamsAsync()(implicit ec: ExecutionContext): Future[Try[UacListMyTeamsResponse]] = {
    val __query = Map[String,String](
    )
    val body: Any = null
    return client.request[Any, UacListMyTeamsResponse]("GET", basePath + s"/team/listMyTeams", __query, body)
  }

  def listMyTeams()(implicit ec: ExecutionContext): Try[UacListMyTeamsResponse] = Await.result(listMyTeamsAsync(), Duration.Inf)

  def listUsersAsync(team_id: String)(implicit ec: ExecutionContext): Future[Try[UacListTeamUserResponse]] = {
    val __query = Map[String,String](
      "team_id" -> client.toQuery(team_id)
    )
    val body: Any = null
    return client.request[Any, UacListTeamUserResponse]("GET", basePath + s"/team/listUsers", __query, body)
  }

  def listUsers(team_id: String)(implicit ec: ExecutionContext): Try[UacListTeamUserResponse] = Await.result(listUsersAsync(team_id), Duration.Inf)

  def removeUserAsync(body: UacRemoveTeamUser)(implicit ec: ExecutionContext): Future[Try[UacRemoveTeamUserResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacRemoveTeamUser, UacRemoveTeamUserResponse]("POST", basePath + s"/team/removeUser", __query, body)
  }

  def removeUser(body: UacRemoveTeamUser)(implicit ec: ExecutionContext): Try[UacRemoveTeamUserResponse] = Await.result(removeUserAsync(body), Duration.Inf)

  def setTeamAsync(body: UacSetTeam)(implicit ec: ExecutionContext): Future[Try[UacSetTeamResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacSetTeam, UacSetTeamResponse]("POST", basePath + s"/team/setTeam", __query, body)
  }

  def setTeam(body: UacSetTeam)(implicit ec: ExecutionContext): Try[UacSetTeamResponse] = Await.result(setTeamAsync(body), Duration.Inf)

}
