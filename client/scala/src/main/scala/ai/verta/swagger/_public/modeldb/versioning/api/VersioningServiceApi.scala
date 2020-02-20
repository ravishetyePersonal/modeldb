// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.versioning.model._

class VersioningServiceApi(client: Client, val basePath: String = "/v1") {
  def ComputeRepositoryDiffAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String, commitA: String, commitB: String, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningComputeRepositoryDiffRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId),
      "commit_a" -> client.toQuery(commitA),
      "commit_b" -> client.toQuery(commitB),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    val body: Any = null
    return client.request[Any, VersioningComputeRepositoryDiffRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/diff", __query, body)
  }

  def ComputeRepositoryDiff(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String, commitA: String, commitB: String, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningComputeRepositoryDiffRequestResponse] = Await.result(ComputeRepositoryDiffAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, repositoryIdRepoId, commitA, commitB, pathPrefix), Duration.Inf)

  def ComputeRepositoryDiff2Async(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, commitA: String, commitB: String, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningComputeRepositoryDiffRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName),
      "commit_a" -> client.toQuery(commitA),
      "commit_b" -> client.toQuery(commitB),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    val body: Any = null
    return client.request[Any, VersioningComputeRepositoryDiffRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/diff", __query, body)
  }

  def ComputeRepositoryDiff2(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, commitA: String, commitB: String, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningComputeRepositoryDiffRequestResponse] = Await.result(ComputeRepositoryDiff2Async(repositoryIdRepoId, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName, commitA, commitB, pathPrefix), Duration.Inf)

  def CreateCommitAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Future[Try[VersioningCreateCommitRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningCreateCommitRequest, VersioningCreateCommitRequestResponse]("POST", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits", __query, body)
  }

  def CreateCommit(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Try[VersioningCreateCommitRequestResponse] = Await.result(CreateCommitAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, body), Duration.Inf)

  def CreateCommit2Async(repositoryIdRepoId: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Future[Try[VersioningCreateCommitRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningCreateCommitRequest, VersioningCreateCommitRequestResponse]("PUT", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits", __query, body)
  }

  def CreateCommit2(repositoryIdRepoId: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Try[VersioningCreateCommitRequestResponse] = Await.result(CreateCommit2Async(repositoryIdRepoId, body), Duration.Inf)

  def CreateRepositoryAsync(idNamedIdWorkspaceName: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (idNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"idNamedIdWorkspaceName\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("POST", basePath + s"/versioning/workspaces/$idNamedIdWorkspaceName/repositories", __query, body)
  }

  def CreateRepository(idNamedIdWorkspaceName: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(CreateRepositoryAsync(idNamedIdWorkspaceName, body), Duration.Inf)

  def DeleteCommitAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteCommitRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits/$commitSha", __query, body)
  }

  def DeleteCommit(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningDeleteCommitRequestResponse] = Await.result(DeleteCommitAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, commitSha, repositoryIdRepoId), Duration.Inf)

  def DeleteCommit2Async(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteCommitRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits/$commitSha", __query, body)
  }

  def DeleteCommit2(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningDeleteCommitRequestResponse] = Await.result(DeleteCommit2Async(repositoryIdRepoId, commitSha, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def DeleteRepositoryAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteRepositoryRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName", __query, body)
  }

  def DeleteRepository(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningDeleteRepositoryRequestResponse] = Await.result(DeleteRepositoryAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, repositoryIdRepoId), Duration.Inf)

  def DeleteRepository2Async(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteRepositoryRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repositoryIdRepoId", __query, body)
  }

  def DeleteRepository2(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningDeleteRepositoryRequestResponse] = Await.result(DeleteRepository2Async(repositoryIdRepoId, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def DeleteTagAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteTagRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/tags/$tag", __query, body)
  }

  def DeleteTag(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningDeleteTagRequestResponse] = Await.result(DeleteTagAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, tag, repositoryIdRepoId), Duration.Inf)

  def DeleteTag2Async(repositoryIdRepoId: String, tag: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteTagRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repositoryIdRepoId/tags/$tag", __query, body)
  }

  def DeleteTag2(repositoryIdRepoId: String, tag: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningDeleteTagRequestResponse] = Await.result(DeleteTag2Async(repositoryIdRepoId, tag, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def GetCommitAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits/$commitSha", __query, body)
  }

  def GetCommit(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitRequestResponse] = Await.result(GetCommitAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, commitSha, repositoryIdRepoId), Duration.Inf)

  def GetCommit2Async(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits/$commitSha", __query, body)
  }

  def GetCommit2(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitRequestResponse] = Await.result(GetCommit2Async(repositoryIdRepoId, commitSha, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def GetCommitBlobAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitBlobRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId),
      "path" -> client.toQuery(path)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitBlobRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits/$commitSha/blobs/path", __query, body)
  }

  def GetCommitBlob(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitBlobRequestResponse] = Await.result(GetCommitBlobAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, commitSha, repositoryIdRepoId, path), Duration.Inf)

  def GetCommitBlob2Async(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitBlobRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName),
      "path" -> client.toQuery(path)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitBlobRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits/$commitSha/blobs/path", __query, body)
  }

  def GetCommitBlob2(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitBlobRequestResponse] = Await.result(GetCommitBlob2Async(repositoryIdRepoId, commitSha, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName, path), Duration.Inf)

  def GetCommitFolderAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitFolderRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId),
      "path" -> client.toQuery(path)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitFolderRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits/$commitSha/folders/path", __query, body)
  }

  def GetCommitFolder(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitFolderRequestResponse] = Await.result(GetCommitFolderAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, commitSha, repositoryIdRepoId, path), Duration.Inf)

  def GetCommitFolder2Async(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitFolderRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName),
      "path" -> client.toQuery(path)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitFolderRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits/$commitSha/folders/path", __query, body)
  }

  def GetCommitFolder2(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitFolderRequestResponse] = Await.result(GetCommitFolder2Async(repositoryIdRepoId, commitSha, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName, path), Duration.Inf)

  def GetRepositoryAsync(idNamedIdWorkspaceName: String, idNamedIdName: String, idRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "id.repo_id" -> client.toQuery(idRepoId)
    )
    if (idNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"idNamedIdWorkspaceName\"")
    if (idNamedIdName == null) throw new Exception("Missing required parameter \"idNamedIdName\"")
    val body: Any = null
    return client.request[Any, VersioningGetRepositoryRequestResponse]("GET", basePath + s"/versioning/workspaces/$idNamedIdWorkspaceName/repositories/$idNamedIdName", __query, body)
  }

  def GetRepository(idNamedIdWorkspaceName: String, idNamedIdName: String, idRepoId: String)(implicit ec: ExecutionContext): Try[VersioningGetRepositoryRequestResponse] = Await.result(GetRepositoryAsync(idNamedIdWorkspaceName, idNamedIdName, idRepoId), Duration.Inf)

  def GetRepository2Async(idRepoId: String, idNamedIdName: String, idNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "id.named_id.name" -> client.toQuery(idNamedIdName),
      "id.named_id.workspace_name" -> client.toQuery(idNamedIdWorkspaceName)
    )
    if (idRepoId == null) throw new Exception("Missing required parameter \"idRepoId\"")
    val body: Any = null
    return client.request[Any, VersioningGetRepositoryRequestResponse]("GET", basePath + s"/versioning/repositories/$idRepoId", __query, body)
  }

  def GetRepository2(idRepoId: String, idNamedIdName: String, idNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningGetRepositoryRequestResponse] = Await.result(GetRepository2Async(idRepoId, idNamedIdName, idNamedIdWorkspaceName), Duration.Inf)

  def GetTagAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningGetTagRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/tags/$tag", __query, body)
  }

  def GetTag(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningGetTagRequestResponse] = Await.result(GetTagAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, tag, repositoryIdRepoId), Duration.Inf)

  def GetTag2Async(repositoryIdRepoId: String, tag: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningGetTagRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/tags/$tag", __query, body)
  }

  def GetTag2(repositoryIdRepoId: String, tag: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningGetTagRequestResponse] = Await.result(GetTag2Async(repositoryIdRepoId, tag, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def ListCommitBlobsAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, paginationPageNumber: Integer, paginationPageLimit: Integer, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitBlobsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId),
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitBlobsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits/$commitSha/blobs", __query, body)
  }

  def ListCommitBlobs(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, commitSha: String, repositoryIdRepoId: String, paginationPageNumber: Integer, paginationPageLimit: Integer, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitBlobsRequestResponse] = Await.result(ListCommitBlobsAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, commitSha, repositoryIdRepoId, paginationPageNumber, paginationPageLimit, pathPrefix), Duration.Inf)

  def ListCommitBlobs2Async(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitBlobsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName),
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (commitSha == null) throw new Exception("Missing required parameter \"commitSha\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitBlobsRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits/$commitSha/blobs", __query, body)
  }

  def ListCommitBlobs2(repositoryIdRepoId: String, commitSha: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitBlobsRequestResponse] = Await.result(ListCommitBlobs2Async(repositoryIdRepoId, commitSha, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName, paginationPageNumber, paginationPageLimit, pathPrefix), Duration.Inf)

  def ListCommitsAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String, paginationPageNumber: Integer, paginationPageLimit: Integer, commitBase: String, commitHead: String, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId),
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit),
      "commit_base" -> client.toQuery(commitBase),
      "commit_head" -> client.toQuery(commitHead),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/commits", __query, body)
  }

  def ListCommits(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String, paginationPageNumber: Integer, paginationPageLimit: Integer, commitBase: String, commitHead: String, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitsRequestResponse] = Await.result(ListCommitsAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, repositoryIdRepoId, paginationPageNumber, paginationPageLimit, commitBase, commitHead, pathPrefix), Duration.Inf)

  def ListCommits2Async(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer, commitBase: String, commitHead: String, pathPrefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName),
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit),
      "commit_base" -> client.toQuery(commitBase),
      "commit_head" -> client.toQuery(commitHead),
      "path_prefix" -> client.toQuery(pathPrefix)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitsRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/commits", __query, body)
  }

  def ListCommits2(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer, commitBase: String, commitHead: String, pathPrefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitsRequestResponse] = Await.result(ListCommits2Async(repositoryIdRepoId, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName, paginationPageNumber, paginationPageLimit, commitBase, commitHead, pathPrefix), Duration.Inf)

  def ListRepositoriesAsync(workspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer)(implicit ec: ExecutionContext): Future[Try[VersioningListRepositoriesRequestResponse]] = {
    val __query = Map[String,String](
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit)
    )
    if (workspaceName == null) throw new Exception("Missing required parameter \"workspaceName\"")
    val body: Any = null
    return client.request[Any, VersioningListRepositoriesRequestResponse]("GET", basePath + s"/versioning/workspaces/$workspaceName/repositories", __query, body)
  }

  def ListRepositories(workspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer)(implicit ec: ExecutionContext): Try[VersioningListRepositoriesRequestResponse] = Await.result(ListRepositoriesAsync(workspaceName, paginationPageNumber, paginationPageLimit), Duration.Inf)

  def ListRepositories2Async(workspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer)(implicit ec: ExecutionContext): Future[Try[VersioningListRepositoriesRequestResponse]] = {
    val __query = Map[String,String](
      "workspace_name" -> client.toQuery(workspaceName),
      "pagination.page_number" -> client.toQuery(paginationPageNumber),
      "pagination.page_limit" -> client.toQuery(paginationPageLimit)
    )
    val body: Any = null
    return client.request[Any, VersioningListRepositoriesRequestResponse]("GET", basePath + s"/versioning/repositories", __query, body)
  }

  def ListRepositories2(workspaceName: String, paginationPageNumber: Integer, paginationPageLimit: Integer)(implicit ec: ExecutionContext): Try[VersioningListRepositoriesRequestResponse] = Await.result(ListRepositories2Async(workspaceName, paginationPageNumber, paginationPageLimit), Duration.Inf)

  def ListTagsAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Future[Try[VersioningListTagsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repositoryIdRepoId)
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    val body: Any = null
    return client.request[Any, VersioningListTagsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/tags", __query, body)
  }

  def ListTags(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, repositoryIdRepoId: String)(implicit ec: ExecutionContext): Try[VersioningListTagsRequestResponse] = Await.result(ListTagsAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, repositoryIdRepoId), Duration.Inf)

  def ListTags2Async(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Future[Try[VersioningListTagsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repositoryIdNamedIdName),
      "repository_id.named_id.workspace_name" -> client.toQuery(repositoryIdNamedIdWorkspaceName)
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    val body: Any = null
    return client.request[Any, VersioningListTagsRequestResponse]("GET", basePath + s"/versioning/repositories/$repositoryIdRepoId/tags", __query, body)
  }

  def ListTags2(repositoryIdRepoId: String, repositoryIdNamedIdName: String, repositoryIdNamedIdWorkspaceName: String)(implicit ec: ExecutionContext): Try[VersioningListTagsRequestResponse] = Await.result(ListTags2Async(repositoryIdRepoId, repositoryIdNamedIdName, repositoryIdNamedIdWorkspaceName), Duration.Inf)

  def SetTagAsync(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, body: String)(implicit ec: ExecutionContext): Future[Try[VersioningSetTagRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repositoryIdNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdWorkspaceName\"")
    if (repositoryIdNamedIdName == null) throw new Exception("Missing required parameter \"repositoryIdNamedIdName\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[String, VersioningSetTagRequestResponse]("PUT", basePath + s"/versioning/workspaces/$repositoryIdNamedIdWorkspaceName/repositories/$repositoryIdNamedIdName/tags/$tag", __query, body)
  }

  def SetTag(repositoryIdNamedIdWorkspaceName: String, repositoryIdNamedIdName: String, tag: String, body: String)(implicit ec: ExecutionContext): Try[VersioningSetTagRequestResponse] = Await.result(SetTagAsync(repositoryIdNamedIdWorkspaceName, repositoryIdNamedIdName, tag, body), Duration.Inf)

  def SetTag2Async(repositoryIdRepoId: String, tag: String, body: String)(implicit ec: ExecutionContext): Future[Try[VersioningSetTagRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repositoryIdRepoId == null) throw new Exception("Missing required parameter \"repositoryIdRepoId\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[String, VersioningSetTagRequestResponse]("PUT", basePath + s"/versioning/repositories/$repositoryIdRepoId/tags/$tag", __query, body)
  }

  def SetTag2(repositoryIdRepoId: String, tag: String, body: String)(implicit ec: ExecutionContext): Try[VersioningSetTagRequestResponse] = Await.result(SetTag2Async(repositoryIdRepoId, tag, body), Duration.Inf)

  def UpdateRepositoryAsync(idNamedIdWorkspaceName: String, idNamedIdName: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (idNamedIdWorkspaceName == null) throw new Exception("Missing required parameter \"idNamedIdWorkspaceName\"")
    if (idNamedIdName == null) throw new Exception("Missing required parameter \"idNamedIdName\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("PUT", basePath + s"/versioning/workspaces/$idNamedIdWorkspaceName/repositories/$idNamedIdName", __query, body)
  }

  def UpdateRepository(idNamedIdWorkspaceName: String, idNamedIdName: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(UpdateRepositoryAsync(idNamedIdWorkspaceName, idNamedIdName, body), Duration.Inf)

  def UpdateRepository2Async(idRepoId: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (idRepoId == null) throw new Exception("Missing required parameter \"idRepoId\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("PUT", basePath + s"/versioning/repositories/$idRepoId", __query, body)
  }

  def UpdateRepository2(idRepoId: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(UpdateRepository2Async(idRepoId, body), Duration.Inf)

}
