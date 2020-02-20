
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.model._

class ExperimentServiceApi(client: Client, val basePath: String = "/v1") {

  def addAttributeAsync(body: ModeldbAddAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbAddAttributesResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddAttributes, ModeldbAddAttributesResponse]("POST", basePath + s"/experiment/addAttribute", __query, body)
  }

  def addAttribute(body: ModeldbAddAttributes)(implicit ec: ExecutionContext): Try[ModeldbAddAttributesResponse] = Await.result(addAttributeAsync(body), Duration.Inf)


  def addExperimentAttributesAsync(body: ModeldbAddExperimentAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbAddExperimentAttributesResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddExperimentAttributes, ModeldbAddExperimentAttributesResponse]("POST", basePath + s"/experiment/addExperimentAttributes", __query, body)
  }

  def addExperimentAttributes(body: ModeldbAddExperimentAttributes)(implicit ec: ExecutionContext): Try[ModeldbAddExperimentAttributesResponse] = Await.result(addExperimentAttributesAsync(body), Duration.Inf)


  def addExperimentTagAsync(body: ModeldbAddExperimentTag)(implicit ec: ExecutionContext): Future[Try[ModeldbAddExperimentTagResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddExperimentTag, ModeldbAddExperimentTagResponse]("POST", basePath + s"/experiment/addExperimentTag", __query, body)
  }

  def addExperimentTag(body: ModeldbAddExperimentTag)(implicit ec: ExecutionContext): Try[ModeldbAddExperimentTagResponse] = Await.result(addExperimentTagAsync(body), Duration.Inf)


  def addExperimentTagsAsync(body: ModeldbAddExperimentTags)(implicit ec: ExecutionContext): Future[Try[ModeldbAddExperimentTagsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddExperimentTags, ModeldbAddExperimentTagsResponse]("POST", basePath + s"/experiment/addExperimentTags", __query, body)
  }

  def addExperimentTags(body: ModeldbAddExperimentTags)(implicit ec: ExecutionContext): Try[ModeldbAddExperimentTagsResponse] = Await.result(addExperimentTagsAsync(body), Duration.Inf)


  def createExperimentAsync(body: ModeldbCreateExperiment)(implicit ec: ExecutionContext): Future[Try[ModeldbCreateExperimentResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbCreateExperiment, ModeldbCreateExperimentResponse]("POST", basePath + s"/experiment/createExperiment", __query, body)
  }

  def createExperiment(body: ModeldbCreateExperiment)(implicit ec: ExecutionContext): Try[ModeldbCreateExperimentResponse] = Await.result(createExperimentAsync(body), Duration.Inf)


  def deleteArtifactAsync(body: ModeldbDeleteExperimentArtifact)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentArtifactResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteExperimentArtifact, ModeldbDeleteExperimentArtifactResponse]("DELETE", basePath + s"/experiment/deleteArtifact", __query, body)
  }

  def deleteArtifact(body: ModeldbDeleteExperimentArtifact)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentArtifactResponse] = Await.result(deleteArtifactAsync(body), Duration.Inf)


  def deleteExperimentAsync(body: ModeldbDeleteExperiment)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteExperiment, ModeldbDeleteExperimentResponse]("DELETE", basePath + s"/experiment/deleteExperiment", __query, body)
  }

  def deleteExperiment(body: ModeldbDeleteExperiment)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentResponse] = Await.result(deleteExperimentAsync(body), Duration.Inf)


  def deleteExperimentAttributesAsync(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "delete_all" -> client.toQuery(deleteAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbDeleteExperimentAttributesResponse]("DELETE", basePath + s"/experiment/deleteExperimentAttributes", __query, body)
  }

  def deleteExperimentAttributes(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentAttributesResponse] = Await.result(deleteExperimentAttributesAsync(id, attributeKeys, deleteAll), Duration.Inf)


  def deleteExperimentTagAsync(body: ModeldbDeleteExperimentTag)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentTagResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteExperimentTag, ModeldbDeleteExperimentTagResponse]("DELETE", basePath + s"/experiment/deleteExperimentTag", __query, body)
  }

  def deleteExperimentTag(body: ModeldbDeleteExperimentTag)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentTagResponse] = Await.result(deleteExperimentTagAsync(body), Duration.Inf)


  def deleteExperimentTagsAsync(body: ModeldbDeleteExperimentTags)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentTagsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteExperimentTags, ModeldbDeleteExperimentTagsResponse]("DELETE", basePath + s"/experiment/deleteExperimentTags", __query, body)
  }

  def deleteExperimentTags(body: ModeldbDeleteExperimentTags)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentTagsResponse] = Await.result(deleteExperimentTagsAsync(body), Duration.Inf)


  def deleteExperimentsAsync(body: ModeldbDeleteExperiments)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteExperimentsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteExperiments, ModeldbDeleteExperimentsResponse]("DELETE", basePath + s"/experiment/deleteExperiments", __query, body)
  }

  def deleteExperiments(body: ModeldbDeleteExperiments)(implicit ec: ExecutionContext): Try[ModeldbDeleteExperimentsResponse] = Await.result(deleteExperimentsAsync(body), Duration.Inf)


  def findExperimentsAsync(body: ModeldbFindExperiments)(implicit ec: ExecutionContext): Future[Try[ModeldbFindExperimentsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindExperiments, ModeldbFindExperimentsResponse]("POST", basePath + s"/experiment/findExperiments", __query, body)
  }

  def findExperiments(body: ModeldbFindExperiments)(implicit ec: ExecutionContext): Try[ModeldbFindExperimentsResponse] = Await.result(findExperimentsAsync(body), Duration.Inf)


  def getArtifactsAsync(id: String, key: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetArtifactsResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "key" -> client.toQuery(key)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetArtifactsResponse]("GET", basePath + s"/experiment/getArtifacts", __query, body)
  }

  def getArtifacts(id: String, key: String)(implicit ec: ExecutionContext): Try[ModeldbGetArtifactsResponse] = Await.result(getArtifactsAsync(id, key), Duration.Inf)


  def getExperimentAttributesAsync(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbGetAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "get_all" -> client.toQuery(getAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetAttributesResponse]("GET", basePath + s"/experiment/getExperimentAttributes", __query, body)
  }

  def getExperimentAttributes(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbGetAttributesResponse] = Await.result(getExperimentAttributesAsync(id, attributeKeys, getAll), Duration.Inf)


  def getExperimentByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetExperimentByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetExperimentByIdResponse]("GET", basePath + s"/experiment/getExperimentById", __query, body)
  }

  def getExperimentById(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetExperimentByIdResponse] = Await.result(getExperimentByIdAsync(id), Duration.Inf)


  def getExperimentByNameAsync(name: String, projectId: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetExperimentByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "project_id" -> client.toQuery(projectId)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetExperimentByNameResponse]("GET", basePath + s"/experiment/getExperimentByName", __query, body)
  }

  def getExperimentByName(name: String, projectId: String)(implicit ec: ExecutionContext): Try[ModeldbGetExperimentByNameResponse] = Await.result(getExperimentByNameAsync(name, projectId), Duration.Inf)


  def getExperimentCodeVersionAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetExperimentCodeVersionResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetExperimentCodeVersionResponse]("GET", basePath + s"/experiment/getExperimentCodeVersion", __query, body)
  }

  def getExperimentCodeVersion(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetExperimentCodeVersionResponse] = Await.result(getExperimentCodeVersionAsync(id), Duration.Inf)


  def getExperimentTagsAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetTagsResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetTagsResponse]("GET", basePath + s"/experiment/getExperimentTags", __query, body)
  }

  def getExperimentTags(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetTagsResponse] = Await.result(getExperimentTagsAsync(id), Duration.Inf)


  def getExperimentsInProjectAsync(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetExperimentsInProjectResponse]] = {
    val __query = Map[String,String](
      "project_id" -> client.toQuery(projectId),
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetExperimentsInProjectResponse]("GET", basePath + s"/experiment/getExperimentsInProject", __query, body)
  }

  def getExperimentsInProject(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Try[ModeldbGetExperimentsInProjectResponse] = Await.result(getExperimentsInProjectAsync(projectId, pageNumber, pageLimit, ascending, sortKey), Duration.Inf)


  def getUrlForArtifactAsync(body: ModeldbGetUrlForArtifact)(implicit ec: ExecutionContext): Future[Try[ModeldbGetUrlForArtifactResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbGetUrlForArtifact, ModeldbGetUrlForArtifactResponse]("POST", basePath + s"/experiment/getUrlForArtifact", __query, body)
  }

  def getUrlForArtifact(body: ModeldbGetUrlForArtifact)(implicit ec: ExecutionContext): Try[ModeldbGetUrlForArtifactResponse] = Await.result(getUrlForArtifactAsync(body), Duration.Inf)


  def logArtifactsAsync(body: ModeldbLogExperimentArtifacts)(implicit ec: ExecutionContext): Future[Try[ModeldbLogExperimentArtifactsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbLogExperimentArtifacts, ModeldbLogExperimentArtifactsResponse]("POST", basePath + s"/experiment/logArtifacts", __query, body)
  }

  def logArtifacts(body: ModeldbLogExperimentArtifacts)(implicit ec: ExecutionContext): Try[ModeldbLogExperimentArtifactsResponse] = Await.result(logArtifactsAsync(body), Duration.Inf)


  def logExperimentCodeVersionAsync(body: ModeldbLogExperimentCodeVersion)(implicit ec: ExecutionContext): Future[Try[ModeldbLogExperimentCodeVersionResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbLogExperimentCodeVersion, ModeldbLogExperimentCodeVersionResponse]("POST", basePath + s"/experiment/logExperimentCodeVersion", __query, body)
  }

  def logExperimentCodeVersion(body: ModeldbLogExperimentCodeVersion)(implicit ec: ExecutionContext): Try[ModeldbLogExperimentCodeVersionResponse] = Await.result(logExperimentCodeVersionAsync(body), Duration.Inf)


  def updateExperimentDescriptionAsync(body: ModeldbUpdateExperimentDescription)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateExperimentDescriptionResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateExperimentDescription, ModeldbUpdateExperimentDescriptionResponse]("POST", basePath + s"/experiment/updateExperimentDescription", __query, body)
  }

  def updateExperimentDescription(body: ModeldbUpdateExperimentDescription)(implicit ec: ExecutionContext): Try[ModeldbUpdateExperimentDescriptionResponse] = Await.result(updateExperimentDescriptionAsync(body), Duration.Inf)


  def updateExperimentNameAsync(body: ModeldbUpdateExperimentName)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateExperimentNameResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateExperimentName, ModeldbUpdateExperimentNameResponse]("POST", basePath + s"/experiment/updateExperimentName", __query, body)
  }

  def updateExperimentName(body: ModeldbUpdateExperimentName)(implicit ec: ExecutionContext): Try[ModeldbUpdateExperimentNameResponse] = Await.result(updateExperimentNameAsync(body), Duration.Inf)


  def updateExperimentNameOrDescriptionAsync(body: ModeldbUpdateExperimentNameOrDescription)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateExperimentNameOrDescriptionResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateExperimentNameOrDescription, ModeldbUpdateExperimentNameOrDescriptionResponse]("POST", basePath + s"/experiment/updateExperimentNameOrDescription", __query, body)
  }

  def updateExperimentNameOrDescription(body: ModeldbUpdateExperimentNameOrDescription)(implicit ec: ExecutionContext): Try[ModeldbUpdateExperimentNameOrDescriptionResponse] = Await.result(updateExperimentNameOrDescriptionAsync(body), Duration.Inf)

}
