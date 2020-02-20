
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.uac.model._

class CollaboratorApi(client: Client, val basePath: String = "/v1") {

  def addOrUpdateDatasetCollaboratorAsync(body: UacAddCollaboratorRequest)(implicit ec: ExecutionContext): Future[Try[UacAddCollaboratorRequestResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacAddCollaboratorRequest, UacAddCollaboratorRequestResponse]("POST", basePath + s"/collaborator/addOrUpdateDatasetCollaborator", __query, body)
  }

  def addOrUpdateDatasetCollaborator(body: UacAddCollaboratorRequest)(implicit ec: ExecutionContext): Try[UacAddCollaboratorRequestResponse] = Await.result(addOrUpdateDatasetCollaboratorAsync(body), Duration.Inf)


  def addOrUpdateProjectCollaboratorAsync(body: UacAddCollaboratorRequest)(implicit ec: ExecutionContext): Future[Try[UacAddCollaboratorRequestResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[UacAddCollaboratorRequest, UacAddCollaboratorRequestResponse]("POST", basePath + s"/collaborator/addOrUpdateProjectCollaborator", __query, body)
  }

  def addOrUpdateProjectCollaborator(body: UacAddCollaboratorRequest)(implicit ec: ExecutionContext): Try[UacAddCollaboratorRequestResponse] = Await.result(addOrUpdateProjectCollaboratorAsync(body), Duration.Inf)


  def getDatasetCollaboratorsAsync(entityId: String)(implicit ec: ExecutionContext): Future[Try[UacGetCollaboratorResponse]] = {
    val __query = Map[String,String](
      "entity_id" -> client.toQuery(entityId)
    )
    val body: Any = null
    return client.request[Any, UacGetCollaboratorResponse]("GET", basePath + s"/collaborator/getDatasetCollaborators", __query, body)
  }

  def getDatasetCollaborators(entityId: String)(implicit ec: ExecutionContext): Try[UacGetCollaboratorResponse] = Await.result(getDatasetCollaboratorsAsync(entityId), Duration.Inf)


  def getProjectCollaboratorsAsync(entityId: String)(implicit ec: ExecutionContext): Future[Try[UacGetCollaboratorResponse]] = {
    val __query = Map[String,String](
      "entity_id" -> client.toQuery(entityId)
    )
    val body: Any = null
    return client.request[Any, UacGetCollaboratorResponse]("GET", basePath + s"/collaborator/getProjectCollaborators", __query, body)
  }

  def getProjectCollaborators(entityId: String)(implicit ec: ExecutionContext): Try[UacGetCollaboratorResponse] = Await.result(getProjectCollaboratorsAsync(entityId), Duration.Inf)


  def removeDatasetCollaboratorAsync(entityId: String, shareWith: String, dateDeleted: String, authzEntityType: String)(implicit ec: ExecutionContext): Future[Try[UacRemoveCollaboratorResponse]] = {
    val __query = Map[String,String](
      "entity_id" -> client.toQuery(entityId),
      "share_with" -> client.toQuery(shareWith),
      "date_deleted" -> client.toQuery(dateDeleted),
      "authz_entity_type" -> client.toQuery(authzEntityType)
    )
    val body: Any = null
    return client.request[Any, UacRemoveCollaboratorResponse]("DELETE", basePath + s"/collaborator/removeDatasetCollaborator", __query, body)
  }

  def removeDatasetCollaborator(entityId: String, shareWith: String, dateDeleted: String, authzEntityType: String)(implicit ec: ExecutionContext): Try[UacRemoveCollaboratorResponse] = Await.result(removeDatasetCollaboratorAsync(entityId, shareWith, dateDeleted, authzEntityType), Duration.Inf)


  def removeProjectCollaboratorAsync(entityId: String, shareWith: String, dateDeleted: String, authzEntityType: String)(implicit ec: ExecutionContext): Future[Try[UacRemoveCollaboratorResponse]] = {
    val __query = Map[String,String](
      "entity_id" -> client.toQuery(entityId),
      "share_with" -> client.toQuery(shareWith),
      "date_deleted" -> client.toQuery(dateDeleted),
      "authz_entity_type" -> client.toQuery(authzEntityType)
    )
    val body: Any = null
    return client.request[Any, UacRemoveCollaboratorResponse]("DELETE", basePath + s"/collaborator/removeProjectCollaborator", __query, body)
  }

  def removeProjectCollaborator(entityId: String, shareWith: String, dateDeleted: String, authzEntityType: String)(implicit ec: ExecutionContext): Try[UacRemoveCollaboratorResponse] = Await.result(removeProjectCollaboratorAsync(entityId, shareWith, dateDeleted, authzEntityType), Duration.Inf)

}
