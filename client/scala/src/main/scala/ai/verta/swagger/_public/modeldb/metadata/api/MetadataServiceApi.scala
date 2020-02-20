// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.metadata.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.metadata.model._

class MetadataServiceApi(client: Client, val basePath: String = "/v1") {
  def AddLabelsAsync(body: MetadataAddLabelsRequest)(implicit ec: ExecutionContext): Future[Try[MetadataAddLabelsRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[MetadataAddLabelsRequest, MetadataAddLabelsRequestResponse]("PUT", basePath + s"/metadata/labels", __query, body)
  }

  def AddLabels(body: MetadataAddLabelsRequest)(implicit ec: ExecutionContext): Try[MetadataAddLabelsRequestResponse] = Await.result(AddLabelsAsync(body), Duration.Inf)

  def DeleteLabelsAsync(body: MetadataDeleteLabelsRequest)(implicit ec: ExecutionContext): Future[Try[MetadataDeleteLabelsRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[MetadataDeleteLabelsRequest, MetadataDeleteLabelsRequestResponse]("DELETE", basePath + s"/metadata/delete", __query, body)
  }

  def DeleteLabels(body: MetadataDeleteLabelsRequest)(implicit ec: ExecutionContext): Try[MetadataDeleteLabelsRequestResponse] = Await.result(DeleteLabelsAsync(body), Duration.Inf)

  def GetLabelsAsync(idIdType: String, idIntId: String, idStringId: String)(implicit ec: ExecutionContext): Future[Try[MetadataGetLabelsRequestResponse]] = {
    val __query = Map[String,String](
      "id.id_type" -> client.toQuery(idIdType),
      "id.int_id" -> client.toQuery(idIntId),
      "id.string_id" -> client.toQuery(idStringId)
    )
    val body: Any = null
    return client.request[Any, MetadataGetLabelsRequestResponse]("GET", basePath + s"/metadata/labels", __query, body)
  }

  def GetLabels(idIdType: String, idIntId: String, idStringId: String)(implicit ec: ExecutionContext): Try[MetadataGetLabelsRequestResponse] = Await.result(GetLabelsAsync(idIdType, idIntId, idStringId), Duration.Inf)

}
