
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.model._

class HydratedServiceApi(client: Client, val basePath: String = "/v1") {

  def findHydratedDatasetVersionsAsync(body: ModeldbFindDatasetVersions)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryDatasetVersionsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindDatasetVersions, ModeldbAdvancedQueryDatasetVersionsResponse]("POST", basePath + s"/hydratedData/findHydratedDatasetVersions", __query, body)
  }

  def findHydratedDatasetVersions(body: ModeldbFindDatasetVersions)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryDatasetVersionsResponse] = Await.result(findHydratedDatasetVersionsAsync(body), Duration.Inf)


  def findHydratedDatasetsAsync(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindDatasets, ModeldbAdvancedQueryDatasetsResponse]("POST", basePath + s"/hydratedData/findHydratedDatasets", __query, body)
  }

  def findHydratedDatasets(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryDatasetsResponse] = Await.result(findHydratedDatasetsAsync(body), Duration.Inf)


  def findHydratedDatasetsByOrganizationAsync(body: ModeldbFindHydratedDatasetsByOrganization)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindHydratedDatasetsByOrganization, ModeldbAdvancedQueryDatasetsResponse]("POST", basePath + s"/hydratedData/findHydratedDatasetsByOrganization", __query, body)
  }

  def findHydratedDatasetsByOrganization(body: ModeldbFindHydratedDatasetsByOrganization)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryDatasetsResponse] = Await.result(findHydratedDatasetsByOrganizationAsync(body), Duration.Inf)


  def findHydratedDatasetsByTeamAsync(body: ModeldbFindHydratedDatasetsByTeam)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindHydratedDatasetsByTeam, ModeldbAdvancedQueryDatasetsResponse]("POST", basePath + s"/hydratedData/findHydratedDatasetsByTeam", __query, body)
  }

  def findHydratedDatasetsByTeam(body: ModeldbFindHydratedDatasetsByTeam)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryDatasetsResponse] = Await.result(findHydratedDatasetsByTeamAsync(body), Duration.Inf)


  def findHydratedExperimentRunsAsync(body: ModeldbFindExperimentRuns)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryExperimentRunsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindExperimentRuns, ModeldbAdvancedQueryExperimentRunsResponse]("POST", basePath + s"/hydratedData/findHydratedExperimentRuns", __query, body)
  }

  def findHydratedExperimentRuns(body: ModeldbFindExperimentRuns)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryExperimentRunsResponse] = Await.result(findHydratedExperimentRunsAsync(body), Duration.Inf)


  def findHydratedExperimentsAsync(body: ModeldbFindExperiments)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryExperimentsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindExperiments, ModeldbAdvancedQueryExperimentsResponse]("POST", basePath + s"/hydratedData/findHydratedExperiments", __query, body)
  }

  def findHydratedExperiments(body: ModeldbFindExperiments)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryExperimentsResponse] = Await.result(findHydratedExperimentsAsync(body), Duration.Inf)


  def findHydratedProjectsAsync(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryProjectsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindProjects, ModeldbAdvancedQueryProjectsResponse]("POST", basePath + s"/hydratedData/findHydratedProjects", __query, body)
  }

  def findHydratedProjects(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryProjectsResponse] = Await.result(findHydratedProjectsAsync(body), Duration.Inf)


  def findHydratedProjectsByOrganizationAsync(body: ModeldbFindHydratedProjectsByOrganization)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryProjectsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindHydratedProjectsByOrganization, ModeldbAdvancedQueryProjectsResponse]("POST", basePath + s"/hydratedData/findHydratedProjectsByOrganization", __query, body)
  }

  def findHydratedProjectsByOrganization(body: ModeldbFindHydratedProjectsByOrganization)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryProjectsResponse] = Await.result(findHydratedProjectsByOrganizationAsync(body), Duration.Inf)


  def findHydratedProjectsByTeamAsync(body: ModeldbFindHydratedProjectsByTeam)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryProjectsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindHydratedProjectsByTeam, ModeldbAdvancedQueryProjectsResponse]("POST", basePath + s"/hydratedData/findHydratedProjectsByTeam", __query, body)
  }

  def findHydratedProjectsByTeam(body: ModeldbFindHydratedProjectsByTeam)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryProjectsResponse] = Await.result(findHydratedProjectsByTeamAsync(body), Duration.Inf)


  def findHydratedProjectsByUserAsync(body: ModeldbFindHydratedProjectsByUser)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryProjectsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindHydratedProjectsByUser, ModeldbAdvancedQueryProjectsResponse]("POST", basePath + s"/hydratedData/findHydratedProjectsByUser", __query, body)
  }

  def findHydratedProjectsByUser(body: ModeldbFindHydratedProjectsByUser)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryProjectsResponse] = Await.result(findHydratedProjectsByUserAsync(body), Duration.Inf)


  def findHydratedPublicDatasetsAsync(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryDatasetsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindDatasets, ModeldbAdvancedQueryDatasetsResponse]("POST", basePath + s"/hydratedData/findHydratedPublicDatasets", __query, body)
  }

  def findHydratedPublicDatasets(body: ModeldbFindDatasets)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryDatasetsResponse] = Await.result(findHydratedPublicDatasetsAsync(body), Duration.Inf)


  def findHydratedPublicProjectsAsync(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryProjectsResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindProjects, ModeldbAdvancedQueryProjectsResponse]("POST", basePath + s"/hydratedData/findHydratedPublicProjects", __query, body)
  }

  def findHydratedPublicProjects(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryProjectsResponse] = Await.result(findHydratedPublicProjectsAsync(body), Duration.Inf)


  def getHydratedDatasetByNameAsync(name: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedDatasetByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedDatasetByNameResponse]("GET", basePath + s"/hydratedData/getHydratedDatasetByName", __query, body)
  }

  def getHydratedDatasetByName(name: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedDatasetByNameResponse] = Await.result(getHydratedDatasetByNameAsync(name, workspaceName), Duration.Inf)


  def getHydratedDatasetsByProjectIdAsync(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedDatasetsByProjectIdResponse]] = {
    val __query = Map[String,String](
      "project_id" -> client.toQuery(projectId),
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedDatasetsByProjectIdResponse]("GET", basePath + s"/hydratedData/getHydratedDatasetsByProjectId", __query, body)
  }

  def getHydratedDatasetsByProjectId(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedDatasetsByProjectIdResponse] = Await.result(getHydratedDatasetsByProjectIdAsync(projectId, pageNumber, pageLimit, ascending, sortKey), Duration.Inf)


  def getHydratedExperimentRunByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedExperimentRunByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedExperimentRunByIdResponse]("GET", basePath + s"/hydratedData/getHydratedExperimentRunById", __query, body)
  }

  def getHydratedExperimentRunById(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedExperimentRunByIdResponse] = Await.result(getHydratedExperimentRunByIdAsync(id), Duration.Inf)


  def getHydratedExperimentRunsInProjectAsync(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedExperimentRunsByProjectIdResponse]] = {
    val __query = Map[String,String](
      "project_id" -> client.toQuery(projectId),
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedExperimentRunsByProjectIdResponse]("GET", basePath + s"/hydratedData/getHydratedExperimentRunsInProject", __query, body)
  }

  def getHydratedExperimentRunsInProject(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedExperimentRunsByProjectIdResponse] = Await.result(getHydratedExperimentRunsInProjectAsync(projectId, pageNumber, pageLimit, ascending, sortKey), Duration.Inf)


  def getHydratedExperimentsByProjectIdAsync(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedExperimentsByProjectIdResponse]] = {
    val __query = Map[String,String](
      "project_id" -> client.toQuery(projectId),
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedExperimentsByProjectIdResponse]("GET", basePath + s"/hydratedData/getHydratedExperimentsByProjectId", __query, body)
  }

  def getHydratedExperimentsByProjectId(projectId: String, pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedExperimentsByProjectIdResponse] = Await.result(getHydratedExperimentsByProjectIdAsync(projectId, pageNumber, pageLimit, ascending, sortKey), Duration.Inf)


  def getHydratedProjectByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedProjectByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedProjectByIdResponse]("GET", basePath + s"/hydratedData/getHydratedProjectById", __query, body)
  }

  def getHydratedProjectById(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedProjectByIdResponse] = Await.result(getHydratedProjectByIdAsync(id), Duration.Inf)


  def getHydratedProjectsAsync(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedProjectsResponse]] = {
    val __query = Map[String,String](
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedProjectsResponse]("GET", basePath + s"/hydratedData/getHydratedProjects", __query, body)
  }

  def getHydratedProjects(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedProjectsResponse] = Await.result(getHydratedProjectsAsync(pageNumber, pageLimit, ascending, sortKey, workspaceName), Duration.Inf)


  def getHydratedPublicProjectsAsync(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetHydratedProjectsResponse]] = {
    val __query = Map[String,String](
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetHydratedProjectsResponse]("GET", basePath + s"/hydratedData/getHydratedPublicProjects", __query, body)
  }

  def getHydratedPublicProjects(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetHydratedProjectsResponse] = Await.result(getHydratedPublicProjectsAsync(pageNumber, pageLimit, ascending, sortKey, workspaceName), Duration.Inf)


  def getTopHydratedExperimentRunsAsync(projectId: String, experimentId: String, experimentRunIds: List[String], sortKey: String, ascending: Boolean, topK: Integer, idsOnly: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryExperimentRunsResponse]] = {
    val __query = Map[String,String](
      "project_id" -> client.toQuery(projectId),
      "experiment_id" -> client.toQuery(experimentId),
      "experiment_run_ids" -> client.toQuery(experimentRunIds),
      "sort_key" -> client.toQuery(sortKey),
      "ascending" -> client.toQuery(ascending),
      "top_k" -> client.toQuery(topK),
      "ids_only" -> client.toQuery(idsOnly)
    )
    val body: Any = null
    return client.request[Any, ModeldbAdvancedQueryExperimentRunsResponse]("GET", basePath + s"/hydratedData/getTopHydratedExperimentRuns", __query, body)
  }

  def getTopHydratedExperimentRuns(projectId: String, experimentId: String, experimentRunIds: List[String], sortKey: String, ascending: Boolean, topK: Integer, idsOnly: Boolean)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryExperimentRunsResponse] = Await.result(getTopHydratedExperimentRunsAsync(projectId, experimentId, experimentRunIds, sortKey, ascending, topK, idsOnly), Duration.Inf)


  def sortHydratedExperimentRunsAsync(experimentRunIds: List[String], sortKey: String, ascending: Boolean, idsOnly: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbAdvancedQueryExperimentRunsResponse]] = {
    val __query = Map[String,String](
      "experiment_run_ids" -> client.toQuery(experimentRunIds),
      "sort_key" -> client.toQuery(sortKey),
      "ascending" -> client.toQuery(ascending),
      "ids_only" -> client.toQuery(idsOnly)
    )
    val body: Any = null
    return client.request[Any, ModeldbAdvancedQueryExperimentRunsResponse]("GET", basePath + s"/hydratedData/sortHydratedExperimentRuns", __query, body)
  }

  def sortHydratedExperimentRuns(experimentRunIds: List[String], sortKey: String, ascending: Boolean, idsOnly: Boolean)(implicit ec: ExecutionContext): Try[ModeldbAdvancedQueryExperimentRunsResponse] = Await.result(sortHydratedExperimentRunsAsync(experimentRunIds, sortKey, ascending, idsOnly), Duration.Inf)

}
