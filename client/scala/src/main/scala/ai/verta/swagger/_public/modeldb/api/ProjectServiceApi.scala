// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.model._

class ProjectServiceApi(client: Client, val basePath: String = "/v1") {
  def addProjectAttributesAsync(body: ModeldbAddProjectAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbAddProjectAttributesResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddProjectAttributes, ModeldbAddProjectAttributesResponse]("POST", basePath + s"/project/addProjectAttributes", __query, body)
  }

  def addProjectAttributes(body: ModeldbAddProjectAttributes)(implicit ec: ExecutionContext): Try[ModeldbAddProjectAttributesResponse] = Await.result(addProjectAttributesAsync(body), Duration.Inf)

  def addProjectTagAsync(body: ModeldbAddProjectTag)(implicit ec: ExecutionContext): Future[Try[ModeldbAddProjectTagResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddProjectTag, ModeldbAddProjectTagResponse]("POST", basePath + s"/project/addProjectTag", __query, body)
  }

  def addProjectTag(body: ModeldbAddProjectTag)(implicit ec: ExecutionContext): Try[ModeldbAddProjectTagResponse] = Await.result(addProjectTagAsync(body), Duration.Inf)

  def addProjectTagsAsync(body: ModeldbAddProjectTags)(implicit ec: ExecutionContext): Future[Try[ModeldbAddProjectTagsResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbAddProjectTags, ModeldbAddProjectTagsResponse]("POST", basePath + s"/project/addProjectTags", __query, body)
  }

  def addProjectTags(body: ModeldbAddProjectTags)(implicit ec: ExecutionContext): Try[ModeldbAddProjectTagsResponse] = Await.result(addProjectTagsAsync(body), Duration.Inf)

  def createProjectAsync(body: ModeldbCreateProject)(implicit ec: ExecutionContext): Future[Try[ModeldbCreateProjectResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbCreateProject, ModeldbCreateProjectResponse]("POST", basePath + s"/project/createProject", __query, body)
  }

  def createProject(body: ModeldbCreateProject)(implicit ec: ExecutionContext): Try[ModeldbCreateProjectResponse] = Await.result(createProjectAsync(body), Duration.Inf)

  def deepCopyProjectAsync(body: ModeldbDeepCopyProject)(implicit ec: ExecutionContext): Future[Try[ModeldbDeepCopyProjectResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeepCopyProject, ModeldbDeepCopyProjectResponse]("POST", basePath + s"/project/deepCopyProject", __query, body)
  }

  def deepCopyProject(body: ModeldbDeepCopyProject)(implicit ec: ExecutionContext): Try[ModeldbDeepCopyProjectResponse] = Await.result(deepCopyProjectAsync(body), Duration.Inf)

  def deleteArtifactAsync(body: ModeldbDeleteProjectArtifact)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectArtifactResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteProjectArtifact, ModeldbDeleteProjectArtifactResponse]("DELETE", basePath + s"/project/deleteArtifact", __query, body)
  }

  def deleteArtifact(body: ModeldbDeleteProjectArtifact)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectArtifactResponse] = Await.result(deleteArtifactAsync(body), Duration.Inf)

  def deleteProjectAsync(body: ModeldbDeleteProject)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteProject, ModeldbDeleteProjectResponse]("DELETE", basePath + s"/project/deleteProject", __query, body)
  }

  def deleteProject(body: ModeldbDeleteProject)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectResponse] = Await.result(deleteProjectAsync(body), Duration.Inf)

  def deleteProjectAttributesAsync(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "delete_all" -> client.toQuery(deleteAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbDeleteProjectAttributesResponse]("DELETE", basePath + s"/project/deleteProjectAttributes", __query, body)
  }

  def deleteProjectAttributes(id: String, attributeKeys: List[String], deleteAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectAttributesResponse] = Await.result(deleteProjectAttributesAsync(id, attributeKeys, deleteAll), Duration.Inf)

  def deleteProjectTagAsync(body: ModeldbDeleteProjectTag)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectTagResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteProjectTag, ModeldbDeleteProjectTagResponse]("DELETE", basePath + s"/project/deleteProjectTag", __query, body)
  }

  def deleteProjectTag(body: ModeldbDeleteProjectTag)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectTagResponse] = Await.result(deleteProjectTagAsync(body), Duration.Inf)

  def deleteProjectTagsAsync(body: ModeldbDeleteProjectTags)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectTagsResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteProjectTags, ModeldbDeleteProjectTagsResponse]("DELETE", basePath + s"/project/deleteProjectTags", __query, body)
  }

  def deleteProjectTags(body: ModeldbDeleteProjectTags)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectTagsResponse] = Await.result(deleteProjectTagsAsync(body), Duration.Inf)

  def deleteProjectsAsync(body: ModeldbDeleteProjects)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteProjectsResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbDeleteProjects, ModeldbDeleteProjectsResponse]("DELETE", basePath + s"/project/deleteProjects", __query, body)
  }

  def deleteProjects(body: ModeldbDeleteProjects)(implicit ec: ExecutionContext): Try[ModeldbDeleteProjectsResponse] = Await.result(deleteProjectsAsync(body), Duration.Inf)

  def findProjectsAsync(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Future[Try[ModeldbFindProjectsResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbFindProjects, ModeldbFindProjectsResponse]("POST", basePath + s"/project/findProjects", __query, body)
  }

  def findProjects(body: ModeldbFindProjects)(implicit ec: ExecutionContext): Try[ModeldbFindProjectsResponse] = Await.result(findProjectsAsync(body), Duration.Inf)

  def getArtifactsAsync(id: String, key: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetArtifactsResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "key" -> client.toQuery(key)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetArtifactsResponse]("GET", basePath + s"/project/getArtifacts", __query, body)
  }

  def getArtifacts(id: String, key: String)(implicit ec: ExecutionContext): Try[ModeldbGetArtifactsResponse] = Await.result(getArtifactsAsync(id, key), Duration.Inf)

  def getProjectAttributesAsync(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Future[Try[ModeldbGetAttributesResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "attribute_keys" -> client.toQuery(attributeKeys),
      "get_all" -> client.toQuery(getAll)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetAttributesResponse]("GET", basePath + s"/project/getProjectAttributes", __query, body)
  }

  def getProjectAttributes(id: String, attributeKeys: List[String], getAll: Boolean)(implicit ec: ExecutionContext): Try[ModeldbGetAttributesResponse] = Await.result(getProjectAttributesAsync(id, attributeKeys, getAll), Duration.Inf)

  def getProjectByIdAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectByIdResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectByIdResponse]("GET", basePath + s"/project/getProjectById", __query, body)
  }

  def getProjectById(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectByIdResponse] = Await.result(getProjectByIdAsync(id), Duration.Inf)

  def getProjectByNameAsync(name: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectByNameResponse]] = {
    val __query = Map[String,String](
      "name" -> client.toQuery(name),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectByNameResponse]("GET", basePath + s"/project/getProjectByName", __query, body)
  }

  def getProjectByName(name: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectByNameResponse] = Await.result(getProjectByNameAsync(name, workspaceName), Duration.Inf)

  def getProjectCodeVersionAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectCodeVersionResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectCodeVersionResponse]("GET", basePath + s"/project/getProjectCodeVersion", __query, body)
  }

  def getProjectCodeVersion(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectCodeVersionResponse] = Await.result(getProjectCodeVersionAsync(id), Duration.Inf)

  def getProjectReadmeAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectReadmeResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectReadmeResponse]("GET", basePath + s"/project/getProjectReadme", __query, body)
  }

  def getProjectReadme(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectReadmeResponse] = Await.result(getProjectReadmeAsync(id), Duration.Inf)

  def getProjectShortNameAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectShortNameResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectShortNameResponse]("GET", basePath + s"/project/getProjectShortName", __query, body)
  }

  def getProjectShortName(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectShortNameResponse] = Await.result(getProjectShortNameAsync(id), Duration.Inf)

  def getProjectTagsAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetTagsResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetTagsResponse]("GET", basePath + s"/project/getProjectTags", __query, body)
  }

  def getProjectTags(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetTagsResponse] = Await.result(getProjectTagsAsync(id), Duration.Inf)

  def getProjectsAsync(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetProjectsResponse]] = {
    val __query = Map[String,String](
      "page_number" -> client.toQuery(pageNumber),
      "page_limit" -> client.toQuery(pageLimit),
      "ascending" -> client.toQuery(ascending),
      "sort_key" -> client.toQuery(sortKey),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetProjectsResponse]("GET", basePath + s"/project/getProjects", __query, body)
  }

  def getProjects(pageNumber: Integer, pageLimit: Integer, ascending: Boolean, sortKey: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetProjectsResponse] = Await.result(getProjectsAsync(pageNumber, pageLimit, ascending, sortKey, workspaceName), Duration.Inf)

  def getPublicProjectsAsync(userId: String, workspaceName: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetPublicProjectsResponse]] = {
    val __query = Map[String,String](
      "user_id" -> client.toQuery(userId),
      "workspace_name" -> client.toQuery(workspaceName)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetPublicProjectsResponse]("GET", basePath + s"/project/getPublicProjects", __query, body)
  }

  def getPublicProjects(userId: String, workspaceName: String)(implicit ec: ExecutionContext): Try[ModeldbGetPublicProjectsResponse] = Await.result(getPublicProjectsAsync(userId, workspaceName), Duration.Inf)

  def getSummaryAsync(entityId: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetSummaryResponse]] = {
    val __query = Map[String,String](
      "entityId" -> client.toQuery(entityId)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetSummaryResponse]("GET", basePath + s"/project/getSummary", __query, body)
  }

  def getSummary(entityId: String)(implicit ec: ExecutionContext): Try[ModeldbGetSummaryResponse] = Await.result(getSummaryAsync(entityId), Duration.Inf)

  def getUrlForArtifactAsync(body: ModeldbGetUrlForArtifact)(implicit ec: ExecutionContext): Future[Try[ModeldbGetUrlForArtifactResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbGetUrlForArtifact, ModeldbGetUrlForArtifactResponse]("POST", basePath + s"/project/getUrlForArtifact", __query, body)
  }

  def getUrlForArtifact(body: ModeldbGetUrlForArtifact)(implicit ec: ExecutionContext): Try[ModeldbGetUrlForArtifactResponse] = Await.result(getUrlForArtifactAsync(body), Duration.Inf)

  def logArtifactsAsync(body: ModeldbLogProjectArtifacts)(implicit ec: ExecutionContext): Future[Try[ModeldbLogProjectArtifactsResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbLogProjectArtifacts, ModeldbLogProjectArtifactsResponse]("POST", basePath + s"/project/logArtifacts", __query, body)
  }

  def logArtifacts(body: ModeldbLogProjectArtifacts)(implicit ec: ExecutionContext): Try[ModeldbLogProjectArtifactsResponse] = Await.result(logArtifactsAsync(body), Duration.Inf)

  def logProjectCodeVersionAsync(body: ModeldbLogProjectCodeVersion)(implicit ec: ExecutionContext): Future[Try[ModeldbLogProjectCodeVersionResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbLogProjectCodeVersion, ModeldbLogProjectCodeVersionResponse]("POST", basePath + s"/project/logProjectCodeVersion", __query, body)
  }

  def logProjectCodeVersion(body: ModeldbLogProjectCodeVersion)(implicit ec: ExecutionContext): Try[ModeldbLogProjectCodeVersionResponse] = Await.result(logProjectCodeVersionAsync(body), Duration.Inf)

  def setProjectReadmeAsync(body: ModeldbSetProjectReadme)(implicit ec: ExecutionContext): Future[Try[ModeldbSetProjectReadmeResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetProjectReadme, ModeldbSetProjectReadmeResponse]("POST", basePath + s"/project/setProjectReadme", __query, body)
  }

  def setProjectReadme(body: ModeldbSetProjectReadme)(implicit ec: ExecutionContext): Try[ModeldbSetProjectReadmeResponse] = Await.result(setProjectReadmeAsync(body), Duration.Inf)

  def setProjectShortNameAsync(body: ModeldbSetProjectShortName)(implicit ec: ExecutionContext): Future[Try[ModeldbSetProjectShortNameResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetProjectShortName, ModeldbSetProjectShortNameResponse]("POST", basePath + s"/project/setProjectShortName", __query, body)
  }

  def setProjectShortName(body: ModeldbSetProjectShortName)(implicit ec: ExecutionContext): Try[ModeldbSetProjectShortNameResponse] = Await.result(setProjectShortNameAsync(body), Duration.Inf)

  def setProjectVisibilityAsync(body: ModeldbSetProjectVisibilty)(implicit ec: ExecutionContext): Future[Try[ModeldbSetProjectVisibiltyResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetProjectVisibilty, ModeldbSetProjectVisibiltyResponse]("POST", basePath + s"/project/setProjectVisibility", __query, body)
  }

  def setProjectVisibility(body: ModeldbSetProjectVisibilty)(implicit ec: ExecutionContext): Try[ModeldbSetProjectVisibiltyResponse] = Await.result(setProjectVisibilityAsync(body), Duration.Inf)

  def setProjectWorkspaceAsync(body: ModeldbSetProjectWorkspace)(implicit ec: ExecutionContext): Future[Try[ModeldbSetProjectWorkspaceResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbSetProjectWorkspace, ModeldbSetProjectWorkspaceResponse]("POST", basePath + s"/project/setProjectWorkspace", __query, body)
  }

  def setProjectWorkspace(body: ModeldbSetProjectWorkspace)(implicit ec: ExecutionContext): Try[ModeldbSetProjectWorkspaceResponse] = Await.result(setProjectWorkspaceAsync(body), Duration.Inf)

  def updateProjectAttributesAsync(body: ModeldbUpdateProjectAttributes)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateProjectAttributesResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateProjectAttributes, ModeldbUpdateProjectAttributesResponse]("POST", basePath + s"/project/updateProjectAttributes", __query, body)
  }

  def updateProjectAttributes(body: ModeldbUpdateProjectAttributes)(implicit ec: ExecutionContext): Try[ModeldbUpdateProjectAttributesResponse] = Await.result(updateProjectAttributesAsync(body), Duration.Inf)

  def updateProjectDescriptionAsync(body: ModeldbUpdateProjectDescription)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateProjectDescriptionResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateProjectDescription, ModeldbUpdateProjectDescriptionResponse]("POST", basePath + s"/project/updateProjectDescription", __query, body)
  }

  def updateProjectDescription(body: ModeldbUpdateProjectDescription)(implicit ec: ExecutionContext): Try[ModeldbUpdateProjectDescriptionResponse] = Await.result(updateProjectDescriptionAsync(body), Duration.Inf)

  def updateProjectNameAsync(body: ModeldbUpdateProjectName)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateProjectNameResponse]] = {
    val __query = Map[String,String](
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbUpdateProjectName, ModeldbUpdateProjectNameResponse]("POST", basePath + s"/project/updateProjectName", __query, body)
  }

  def updateProjectName(body: ModeldbUpdateProjectName)(implicit ec: ExecutionContext): Try[ModeldbUpdateProjectNameResponse] = Await.result(updateProjectNameAsync(body), Duration.Inf)

  def verifyConnectionAsync()(implicit ec: ExecutionContext): Future[Try[ModeldbVerifyConnectionResponse]] = {
    val __query = Map[String,String](
    )
    val body: Any = null
    return client.request[Any, ModeldbVerifyConnectionResponse]("GET", basePath + s"/project/verifyConnection", __query, body)
  }

  def verifyConnection()(implicit ec: ExecutionContext): Try[ModeldbVerifyConnectionResponse] = Await.result(verifyConnectionAsync(), Duration.Inf)

}
