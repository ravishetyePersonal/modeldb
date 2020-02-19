
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.model._

class DatasetServiceApi(client: Client, val basePath: String = "/v1") {

  def addDatasetAttributesAsync(body: ModeldbAddDatasetAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbAddDatasetAttributesResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddDatasetAttributes, ModeldbAddDatasetAttributesResponse]("POST", basePath + s"/dataset/addDatasetAttributes", __query, body)
  }

  def addDatasetAttributes(body: ModeldbAddDatasetAttributes)(implicit ec: ExecutionContext): Try[ModeldbAddDatasetAttributesResponse] = Await.result(addDatasetAttributesAsync(body), Duration.Inf)


  def addDatasetTagsAsync(body: ModeldbAddDatasetTags)(implicit ec: ExecutionContext): Future[Try[ModeldbAddDatasetTagsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddDatasetTags, ModeldbAddDatasetTagsResponse]("POST", basePath + s"/dataset/addDatasetTags", __query, body)
  }

  def addDatasetTags(body: ModeldbAddDatasetTags)(implicit ec: ExecutionContext): Try[ModeldbAddDatasetTagsResponse] = Await.result(addDatasetTagsAsync(body), Duration.Inf)


  def createDatasetAsync(body: ModeldbCreateDataset)(implicit ec: ExecutionContext): Future[Try[ModeldbCreateDatasetResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbCreateDataset, ModeldbCreateDatasetResponse]("POST", basePath + s"/dataset/createDataset", __query, body)
  }

  def createDataset(body: ModeldbCreateDataset)(implicit ec: ExecutionContext): Try[ModeldbCreateDatasetResponse] = Await.result(createDatasetAsync(body), Duration.Inf)


  def deleteDatasetAsync(body: ModeldbDeleteDataset)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteDatasetResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteDataset, ModeldbDeleteDatasetResponse]("DELETE", basePath + s"/dataset/deleteDataset", __query, body)
  }

  def deleteDataset(body: ModeldbDeleteDataset)(implicit ec: ExecutionContext): Try[ModeldbDeleteDatasetResponse] = Await.result(deleteDatasetAsync(body), Duration.Inf)


  def deleteDatasetAttributesAsync(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteDatasetAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "delete_all" -> client.toQuery(deleteAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbDeleteDatasetAttributesResponse]("DELETE", basePath + s"/dataset/deleteDatasetAttributes", __query, body)
  }

  def deleteDatasetAttributes(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbDeleteDatasetAttributesResponse] = Await.result(deleteDatasetAttributesAsync(id, attributeKeys, deleteAll), Duration.Inf)


  def deleteDatasetTagsAsync(body: ModeldbDeleteDatasetTags)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteDatasetTagsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteDatasetTags, ModeldbDeleteDatasetTagsResponse]("DELETE", basePath + s"/dataset/deleteDatasetTags", __query, body)
  }

  def deleteDatasetTags(body: ModeldbDeleteDatasetTags)(implicit ec: ExecutionContext): Try[ModeldbDeleteDatasetTagsResponse] = Await.result(deleteDatasetTagsAsync(body), Duration.Inf)


  def deleteDatasetsAsync(body: ModeldbDeleteDatasets)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteDatasets, ModeldbDeleteDatasetsResponse]("DELETE", basePath + s"/dataset/deleteDatasets", __query, body)
  }

  def deleteDatasets(body: ModeldbDeleteDatasets)(implicit ec: ExecutionContext): Try[ModeldbDeleteDatasetsResponse] = Await.result(deleteDatasetsAsync(body), Duration.Inf)


  def findDatasetsAsync(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Future[Try[ModeldbFindDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindDatasets, ModeldbFindDatasetsResponse]("POST", basePath + s"/dataset/findDatasets", __query, body)
  }

  def findDatasets(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Try[ModeldbFindDatasetsResponse] = Await.result(findDatasetsAsync(body), Duration.Inf)


  def getAllDatasetsAsync(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetAllDatasetsResponse]] = {
    val __query = Map[String,String](
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetAllDatasetsResponse]("GET", basePath + s"/dataset/getAllDatasets", __query, body)
  }

  def getAllDatasets(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetAllDatasetsResponse] = Await.result(getAllDatasetsAsync(pageNumber, pageLimit, ascending, sortKey, workspaceName), Duration.Inf)


  def getDatasetAttributesAsync(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbGetAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "get_all" -> client.toQuery(getAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetAttributesResponse]("GET", basePath + s"/dataset/getDatasetAttributes", __query, body)
  }

  def getDatasetAttributes(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbGetAttributesResponse] = Await.result(getDatasetAttributesAsync(id, attributeKeys, getAll), Duration.Inf)


  def getDatasetByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetDatasetByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetDatasetByIdResponse]("GET", basePath + s"/dataset/getDatasetById", __query, body)
  }

  def getDatasetById(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetDatasetByIdResponse] = Await.result(getDatasetByIdAsync(id), Duration.Inf)


  def getDatasetByNameAsync(name: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetDatasetByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetDatasetByNameResponse]("GET", basePath + s"/dataset/getDatasetByName", __query, body)
  }

  def getDatasetByName(name: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetDatasetByNameResponse] = Await.result(getDatasetByNameAsync(name, workspaceName), Duration.Inf)


  def getDatasetTagsAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetTagsResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetTagsResponse]("GET", basePath + s"/dataset/getDatasetTags", __query, body)
  }

  def getDatasetTags(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetTagsResponse] = Await.result(getDatasetTagsAsync(id), Duration.Inf)


  def getExperimentRunByDatasetAsync(body: ModeldbGetExperimentRunByDataset)(implicit ec: ExecutionContext): Future[Try[ModeldbGetExperimentRunByDatasetResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbGetExperimentRunByDataset, ModeldbGetExperimentRunByDatasetResponse]("POST", basePath + s"/dataset/getExperimentRunByDataset", __query, body)
  }

  def getExperimentRunByDataset(body: ModeldbGetExperimentRunByDataset)(implicit ec: ExecutionContext): Try[ModeldbGetExperimentRunByDatasetResponse] = Await.result(getExperimentRunByDatasetAsync(body), Duration.Inf)


  def getLastExperimentByDatasetIdAsync(datasetId: String)(implicit ec: ExecutionContext): Future[Try[ModeldbLastExperimentByDatasetIdResponse]] = {
    val __query = Map[String,String](
      "dataset_id" -> client.toQuery(datasetId)
    )
    val body: Any = null
    return client.request[Any, ModeldbLastExperimentByDatasetIdResponse]("GET", basePath + s"/dataset/getLastExperimentByDatasetId", __query, body)
  }

  def getLastExperimentByDatasetId(datasetId: String)(implicit ec: ExecutionContext): Try[ModeldbLastExperimentByDatasetIdResponse] = Await.result(getLastExperimentByDatasetIdAsync(datasetId), Duration.Inf)


  def setDatasetVisibilityAsync(body: ModeldbSetDatasetVisibilty)(implicit ec: ExecutionContext): Future[Try[ModeldbSetDatasetVisibiltyResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetDatasetVisibilty, ModeldbSetDatasetVisibiltyResponse]("POST", basePath + s"/dataset/setDatasetVisibility", __query, body)
  }

  def setDatasetVisibility(body: ModeldbSetDatasetVisibilty)(implicit ec: ExecutionContext): Try[ModeldbSetDatasetVisibiltyResponse] = Await.result(setDatasetVisibilityAsync(body), Duration.Inf)


  def setDatasetWorkspaceAsync(body: ModeldbSetDatasetWorkspace)(implicit ec: ExecutionContext): Future[Try[ModeldbSetDatasetWorkspaceResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetDatasetWorkspace, ModeldbSetDatasetWorkspaceResponse]("POST", basePath + s"/dataset/setDatasetWorkspace", __query, body)
  }

  def setDatasetWorkspace(body: ModeldbSetDatasetWorkspace)(implicit ec: ExecutionContext): Try[ModeldbSetDatasetWorkspaceResponse] = Await.result(setDatasetWorkspaceAsync(body), Duration.Inf)


  def updateDatasetAttributesAsync(body: ModeldbUpdateDatasetAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateDatasetAttributesResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateDatasetAttributes, ModeldbUpdateDatasetAttributesResponse]("POST", basePath + s"/dataset/updateDatasetAttributes", __query, body)
  }

  def updateDatasetAttributes(body: ModeldbUpdateDatasetAttributes)(implicit ec: ExecutionContext): Try[ModeldbUpdateDatasetAttributesResponse] = Await.result(updateDatasetAttributesAsync(body), Duration.Inf)


  def updateDatasetDescriptionAsync(body: ModeldbUpdateDatasetDescription)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateDatasetDescriptionResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateDatasetDescription, ModeldbUpdateDatasetDescriptionResponse]("POST", basePath + s"/dataset/updateDatasetDescription", __query, body)
  }

  def updateDatasetDescription(body: ModeldbUpdateDatasetDescription)(implicit ec: ExecutionContext): Try[ModeldbUpdateDatasetDescriptionResponse] = Await.result(updateDatasetDescriptionAsync(body), Duration.Inf)


  def updateDatasetNameAsync(body: ModeldbUpdateDatasetName)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateDatasetNameResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateDatasetName, ModeldbUpdateDatasetNameResponse]("POST", basePath + s"/dataset/updateDatasetName", __query, body)
  }

  def updateDatasetName(body: ModeldbUpdateDatasetName)(implicit ec: ExecutionContext): Try[ModeldbUpdateDatasetNameResponse] = Await.result(updateDatasetNameAsync(body), Duration.Inf)

}
