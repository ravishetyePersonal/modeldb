
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.uac.model._

class UACServiceApi(client: Client, val basePath: String = "/v1") {

  def createUserAsync(body: UacCreateUser)(implicit ec: ExecutionContext): Future[Try[UacCreateUserResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacCreateUser, UacCreateUserResponse]("POST", basePath + s"/uac/createUser", __query, body)
  }

  def createUser(body: UacCreateUser)(implicit ec: ExecutionContext): Try[UacCreateUserResponse] = Await.result(createUserAsync(body), Duration.Inf)


  def deleteUserAsync(body: UacDeleteUser)(implicit ec: ExecutionContext): Future[Try[UacDeleteUserResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacDeleteUser, UacDeleteUserResponse]("POST", basePath + s"/uac/deleteUser", __query, body)
  }

  def deleteUser(body: UacDeleteUser)(implicit ec: ExecutionContext): Try[UacDeleteUserResponse] = Await.result(deleteUserAsync(body), Duration.Inf)


  def getCurrentUserAsync()(implicit ec: ExecutionContext): Future[Try[UacUserInfo]] = {
    val __query = Map[String,String](
      
    )
    val body: Any = null
    return client.request[Any, UacUserInfo]("GET", basePath + s"/uac/getCurrentUser", __query, body)
  }

  def getCurrentUser()(implicit ec: ExecutionContext): Try[UacUserInfo] = Await.result(getCurrentUserAsync(), Duration.Inf)


  def getUserAsync(userId: String, email: String, username: String)(implicit ec: ExecutionContext): Future[Try[UacUserInfo]] = {
    val __query = Map[String,String](
      "user_id" -> client.toQuery(userId),
      "email" -> client.toQuery(email),
      "username" -> client.toQuery(username)
    )
    val body: Any = null
    return client.request[Any, UacUserInfo]("GET", basePath + s"/uac/getUser", __query, body)
  }

  def getUser(userId: String, email: String, username: String)(implicit ec: ExecutionContext): Try[UacUserInfo] = Await.result(getUserAsync(userId, email, username), Duration.Inf)


  def getUsersAsync(body: UacGetUsers)(implicit ec: ExecutionContext): Future[Try[UacGetUsersResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacGetUsers, UacGetUsersResponse]("POST", basePath + s"/uac/getUsers", __query, body)
  }

  def getUsers(body: UacGetUsers)(implicit ec: ExecutionContext): Try[UacGetUsersResponse] = Await.result(getUsersAsync(body), Duration.Inf)


  def updateUserAsync(body: UacUpdateUser)(implicit ec: ExecutionContext): Future[Try[UacUpdateUserResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacUpdateUser, UacUpdateUserResponse]("POST", basePath + s"/uac/updateUser", __query, body)
  }

  def updateUser(body: UacUpdateUser)(implicit ec: ExecutionContext): Try[UacUpdateUserResponse] = Await.result(updateUserAsync(body), Duration.Inf)

}
