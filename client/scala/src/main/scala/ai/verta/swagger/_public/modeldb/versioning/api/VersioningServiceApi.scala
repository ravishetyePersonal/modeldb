// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.versioning.model._

class VersioningServiceApi(client: Client, val basePath: String = "/v1") {
  def ComputeRepositoryDiffAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String, commit_a: String, commit_b: String, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningComputeRepositoryDiffRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id),
      "commit_a" -> client.toQuery(commit_a),
      "commit_b" -> client.toQuery(commit_b),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    val body: Any = null
    return client.request[Any, VersioningComputeRepositoryDiffRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/diff", __query, body)
  }

  def ComputeRepositoryDiff(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String, commit_a: String, commit_b: String, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningComputeRepositoryDiffRequestResponse] = Await.result(ComputeRepositoryDiffAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, repository_id_repo_id, commit_a, commit_b, path_prefix), Duration.Inf)

  def ComputeRepositoryDiff2Async(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, commit_a: String, commit_b: String, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningComputeRepositoryDiffRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name),
      "commit_a" -> client.toQuery(commit_a),
      "commit_b" -> client.toQuery(commit_b),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    val body: Any = null
    return client.request[Any, VersioningComputeRepositoryDiffRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/diff", __query, body)
  }

  def ComputeRepositoryDiff2(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, commit_a: String, commit_b: String, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningComputeRepositoryDiffRequestResponse] = Await.result(ComputeRepositoryDiff2Async(repository_id_repo_id, repository_id_named_id_name, repository_id_named_id_workspace_name, commit_a, commit_b, path_prefix), Duration.Inf)

  def CreateCommitAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Future[Try[VersioningCreateCommitRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningCreateCommitRequest, VersioningCreateCommitRequestResponse]("POST", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits", __query, body)
  }

  def CreateCommit(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Try[VersioningCreateCommitRequestResponse] = Await.result(CreateCommitAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, body), Duration.Inf)

  def CreateCommit2Async(repository_id_repo_id: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Future[Try[VersioningCreateCommitRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningCreateCommitRequest, VersioningCreateCommitRequestResponse]("PUT", basePath + s"/versioning/repositories/$repository_id_repo_id/commits", __query, body)
  }

  def CreateCommit2(repository_id_repo_id: String, body: VersioningCreateCommitRequest)(implicit ec: ExecutionContext): Try[VersioningCreateCommitRequestResponse] = Await.result(CreateCommit2Async(repository_id_repo_id, body), Duration.Inf)

  def CreateRepositoryAsync(id_named_id_workspace_name: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"id_named_id_workspace_name\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("POST", basePath + s"/versioning/workspaces/$id_named_id_workspace_name/repositories", __query, body)
  }

  def CreateRepository(id_named_id_workspace_name: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(CreateRepositoryAsync(id_named_id_workspace_name, body), Duration.Inf)

  def DeleteCommitAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteCommitRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits/$commit_sha", __query, body)
  }

  def DeleteCommit(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningDeleteCommitRequestResponse] = Await.result(DeleteCommitAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, commit_sha, repository_id_repo_id), Duration.Inf)

  def DeleteCommit2Async(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteCommitRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repository_id_repo_id/commits/$commit_sha", __query, body)
  }

  def DeleteCommit2(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningDeleteCommitRequestResponse] = Await.result(DeleteCommit2Async(repository_id_repo_id, commit_sha, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def DeleteRepositoryAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteRepositoryRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name", __query, body)
  }

  def DeleteRepository(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningDeleteRepositoryRequestResponse] = Await.result(DeleteRepositoryAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, repository_id_repo_id), Duration.Inf)

  def DeleteRepository2Async(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteRepositoryRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repository_id_repo_id", __query, body)
  }

  def DeleteRepository2(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningDeleteRepositoryRequestResponse] = Await.result(DeleteRepository2Async(repository_id_repo_id, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def DeleteTagAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteTagRequestResponse]("DELETE", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/tags/$tag", __query, body)
  }

  def DeleteTag(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningDeleteTagRequestResponse] = Await.result(DeleteTagAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, tag, repository_id_repo_id), Duration.Inf)

  def DeleteTag2Async(repository_id_repo_id: String, tag: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningDeleteTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningDeleteTagRequestResponse]("DELETE", basePath + s"/versioning/repositories/$repository_id_repo_id/tags/$tag", __query, body)
  }

  def DeleteTag2(repository_id_repo_id: String, tag: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningDeleteTagRequestResponse] = Await.result(DeleteTag2Async(repository_id_repo_id, tag, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def GetCommitAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits/$commit_sha", __query, body)
  }

  def GetCommit(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitRequestResponse] = Await.result(GetCommitAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, commit_sha, repository_id_repo_id), Duration.Inf)

  def GetCommit2Async(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/commits/$commit_sha", __query, body)
  }

  def GetCommit2(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitRequestResponse] = Await.result(GetCommit2Async(repository_id_repo_id, commit_sha, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def GetCommitBlobAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitBlobRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id),
      "path" -> client.toQuery(path)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitBlobRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits/$commit_sha/blobs/path", __query, body)
  }

  def GetCommitBlob(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitBlobRequestResponse] = Await.result(GetCommitBlobAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, commit_sha, repository_id_repo_id, path), Duration.Inf)

  def GetCommitBlob2Async(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitBlobRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name),
      "path" -> client.toQuery(path)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitBlobRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/commits/$commit_sha/blobs/path", __query, body)
  }

  def GetCommitBlob2(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitBlobRequestResponse] = Await.result(GetCommitBlob2Async(repository_id_repo_id, commit_sha, repository_id_named_id_name, repository_id_named_id_workspace_name, path), Duration.Inf)

  def GetCommitFolderAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitFolderRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id),
      "path" -> client.toQuery(path)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitFolderRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits/$commit_sha/folders/path", __query, body)
  }

  def GetCommitFolder(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitFolderRequestResponse] = Await.result(GetCommitFolderAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, commit_sha, repository_id_repo_id, path), Duration.Inf)

  def GetCommitFolder2Async(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, path: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetCommitFolderRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name),
      "path" -> client.toQuery(path)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningGetCommitFolderRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/commits/$commit_sha/folders/path", __query, body)
  }

  def GetCommitFolder2(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, path: String)(implicit ec: ExecutionContext): Try[VersioningGetCommitFolderRequestResponse] = Await.result(GetCommitFolder2Async(repository_id_repo_id, commit_sha, repository_id_named_id_name, repository_id_named_id_workspace_name, path), Duration.Inf)

  def GetRepositoryAsync(id_named_id_workspace_name: String, id_named_id_name: String, id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "id.repo_id" -> client.toQuery(id_repo_id)
    )
    if (id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"id_named_id_workspace_name\"")
    if (id_named_id_name == null) throw new Exception("Missing required parameter \"id_named_id_name\"")
    val body: Any = null
    return client.request[Any, VersioningGetRepositoryRequestResponse]("GET", basePath + s"/versioning/workspaces/$id_named_id_workspace_name/repositories/$id_named_id_name", __query, body)
  }

  def GetRepository(id_named_id_workspace_name: String, id_named_id_name: String, id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningGetRepositoryRequestResponse] = Await.result(GetRepositoryAsync(id_named_id_workspace_name, id_named_id_name, id_repo_id), Duration.Inf)

  def GetRepository2Async(id_repo_id: String, id_named_id_name: String, id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetRepositoryRequestResponse]] = {
    val __query = Map[String,String](
      "id.named_id.name" -> client.toQuery(id_named_id_name),
      "id.named_id.workspace_name" -> client.toQuery(id_named_id_workspace_name)
    )
    if (id_repo_id == null) throw new Exception("Missing required parameter \"id_repo_id\"")
    val body: Any = null
    return client.request[Any, VersioningGetRepositoryRequestResponse]("GET", basePath + s"/versioning/repositories/$id_repo_id", __query, body)
  }

  def GetRepository2(id_repo_id: String, id_named_id_name: String, id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningGetRepositoryRequestResponse] = Await.result(GetRepository2Async(id_repo_id, id_named_id_name, id_named_id_workspace_name), Duration.Inf)

  def GetTagAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningGetTagRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/tags/$tag", __query, body)
  }

  def GetTag(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningGetTagRequestResponse] = Await.result(GetTagAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, tag, repository_id_repo_id), Duration.Inf)

  def GetTag2Async(repository_id_repo_id: String, tag: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningGetTagRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    val body: Any = null
    return client.request[Any, VersioningGetTagRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/tags/$tag", __query, body)
  }

  def GetTag2(repository_id_repo_id: String, tag: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningGetTagRequestResponse] = Await.result(GetTag2Async(repository_id_repo_id, tag, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def ListCommitBlobsAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, pagination_page_number: Integer, pagination_page_limit: Integer, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitBlobsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id),
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitBlobsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits/$commit_sha/blobs", __query, body)
  }

  def ListCommitBlobs(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, commit_sha: String, repository_id_repo_id: String, pagination_page_number: Integer, pagination_page_limit: Integer, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitBlobsRequestResponse] = Await.result(ListCommitBlobsAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, commit_sha, repository_id_repo_id, pagination_page_number, pagination_page_limit, path_prefix), Duration.Inf)

  def ListCommitBlobs2Async(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitBlobsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name),
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (commit_sha == null) throw new Exception("Missing required parameter \"commit_sha\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitBlobsRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/commits/$commit_sha/blobs", __query, body)
  }

  def ListCommitBlobs2(repository_id_repo_id: String, commit_sha: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitBlobsRequestResponse] = Await.result(ListCommitBlobs2Async(repository_id_repo_id, commit_sha, repository_id_named_id_name, repository_id_named_id_workspace_name, pagination_page_number, pagination_page_limit, path_prefix), Duration.Inf)

  def ListCommitsAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String, pagination_page_number: Integer, pagination_page_limit: Integer, commit_base: String, commit_head: String, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id),
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit),
      "commit_base" -> client.toQuery(commit_base),
      "commit_head" -> client.toQuery(commit_head),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/commits", __query, body)
  }

  def ListCommits(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String, pagination_page_number: Integer, pagination_page_limit: Integer, commit_base: String, commit_head: String, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitsRequestResponse] = Await.result(ListCommitsAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, repository_id_repo_id, pagination_page_number, pagination_page_limit, commit_base, commit_head, path_prefix), Duration.Inf)

  def ListCommits2Async(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer, commit_base: String, commit_head: String, path_prefix: String)(implicit ec: ExecutionContext): Future[Try[VersioningListCommitsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name),
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit),
      "commit_base" -> client.toQuery(commit_base),
      "commit_head" -> client.toQuery(commit_head),
      "path_prefix" -> client.toQuery(path_prefix)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    val body: Any = null
    return client.request[Any, VersioningListCommitsRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/commits", __query, body)
  }

  def ListCommits2(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer, commit_base: String, commit_head: String, path_prefix: String)(implicit ec: ExecutionContext): Try[VersioningListCommitsRequestResponse] = Await.result(ListCommits2Async(repository_id_repo_id, repository_id_named_id_name, repository_id_named_id_workspace_name, pagination_page_number, pagination_page_limit, commit_base, commit_head, path_prefix), Duration.Inf)

  def ListRepositoriesAsync(workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer)(implicit ec: ExecutionContext): Future[Try[VersioningListRepositoriesRequestResponse]] = {
    val __query = Map[String,String](
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit)
    )
    if (workspace_name == null) throw new Exception("Missing required parameter \"workspace_name\"")
    val body: Any = null
    return client.request[Any, VersioningListRepositoriesRequestResponse]("GET", basePath + s"/versioning/workspaces/$workspace_name/repositories", __query, body)
  }

  def ListRepositories(workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer)(implicit ec: ExecutionContext): Try[VersioningListRepositoriesRequestResponse] = Await.result(ListRepositoriesAsync(workspace_name, pagination_page_number, pagination_page_limit), Duration.Inf)

  def ListRepositories2Async(workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer)(implicit ec: ExecutionContext): Future[Try[VersioningListRepositoriesRequestResponse]] = {
    val __query = Map[String,String](
      "workspace_name" -> client.toQuery(workspace_name),
      "pagination.page_number" -> client.toQuery(pagination_page_number),
      "pagination.page_limit" -> client.toQuery(pagination_page_limit)
    )
    val body: Any = null
    return client.request[Any, VersioningListRepositoriesRequestResponse]("GET", basePath + s"/versioning/repositories", __query, body)
  }

  def ListRepositories2(workspace_name: String, pagination_page_number: Integer, pagination_page_limit: Integer)(implicit ec: ExecutionContext): Try[VersioningListRepositoriesRequestResponse] = Await.result(ListRepositories2Async(workspace_name, pagination_page_number, pagination_page_limit), Duration.Inf)

  def ListTagsAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Future[Try[VersioningListTagsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.repo_id" -> client.toQuery(repository_id_repo_id)
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    val body: Any = null
    return client.request[Any, VersioningListTagsRequestResponse]("GET", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/tags", __query, body)
  }

  def ListTags(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, repository_id_repo_id: String)(implicit ec: ExecutionContext): Try[VersioningListTagsRequestResponse] = Await.result(ListTagsAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, repository_id_repo_id), Duration.Inf)

  def ListTags2Async(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Future[Try[VersioningListTagsRequestResponse]] = {
    val __query = Map[String,String](
      "repository_id.named_id.name" -> client.toQuery(repository_id_named_id_name),
      "repository_id.named_id.workspace_name" -> client.toQuery(repository_id_named_id_workspace_name)
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    val body: Any = null
    return client.request[Any, VersioningListTagsRequestResponse]("GET", basePath + s"/versioning/repositories/$repository_id_repo_id/tags", __query, body)
  }

  def ListTags2(repository_id_repo_id: String, repository_id_named_id_name: String, repository_id_named_id_workspace_name: String)(implicit ec: ExecutionContext): Try[VersioningListTagsRequestResponse] = Await.result(ListTags2Async(repository_id_repo_id, repository_id_named_id_name, repository_id_named_id_workspace_name), Duration.Inf)

  def SetTagAsync(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, body: String)(implicit ec: ExecutionContext): Future[Try[VersioningSetTagRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repository_id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_workspace_name\"")
    if (repository_id_named_id_name == null) throw new Exception("Missing required parameter \"repository_id_named_id_name\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[String, VersioningSetTagRequestResponse]("PUT", basePath + s"/versioning/workspaces/$repository_id_named_id_workspace_name/repositories/$repository_id_named_id_name/tags/$tag", __query, body)
  }

  def SetTag(repository_id_named_id_workspace_name: String, repository_id_named_id_name: String, tag: String, body: String)(implicit ec: ExecutionContext): Try[VersioningSetTagRequestResponse] = Await.result(SetTagAsync(repository_id_named_id_workspace_name, repository_id_named_id_name, tag, body), Duration.Inf)

  def SetTag2Async(repository_id_repo_id: String, tag: String, body: String)(implicit ec: ExecutionContext): Future[Try[VersioningSetTagRequestResponse]] = {
    val __query = Map[String,String](
    )
    if (repository_id_repo_id == null) throw new Exception("Missing required parameter \"repository_id_repo_id\"")
    if (tag == null) throw new Exception("Missing required parameter \"tag\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[String, VersioningSetTagRequestResponse]("PUT", basePath + s"/versioning/repositories/$repository_id_repo_id/tags/$tag", __query, body)
  }

  def SetTag2(repository_id_repo_id: String, tag: String, body: String)(implicit ec: ExecutionContext): Try[VersioningSetTagRequestResponse] = Await.result(SetTag2Async(repository_id_repo_id, tag, body), Duration.Inf)

  def UpdateRepositoryAsync(id_named_id_workspace_name: String, id_named_id_name: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (id_named_id_workspace_name == null) throw new Exception("Missing required parameter \"id_named_id_workspace_name\"")
    if (id_named_id_name == null) throw new Exception("Missing required parameter \"id_named_id_name\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("PUT", basePath + s"/versioning/workspaces/$id_named_id_workspace_name/repositories/$id_named_id_name", __query, body)
  }

  def UpdateRepository(id_named_id_workspace_name: String, id_named_id_name: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(UpdateRepositoryAsync(id_named_id_workspace_name, id_named_id_name, body), Duration.Inf)

  def UpdateRepository2Async(id_repo_id: String, body: VersioningRepository)(implicit ec: ExecutionContext): Future[Try[VersioningSetRepositoryResponse]] = {
    val __query = Map[String,String](
    )
    if (id_repo_id == null) throw new Exception("Missing required parameter \"id_repo_id\"")
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[VersioningRepository, VersioningSetRepositoryResponse]("PUT", basePath + s"/versioning/repositories/$id_repo_id", __query, body)
  }

  def UpdateRepository2(id_repo_id: String, body: VersioningRepository)(implicit ec: ExecutionContext): Try[VersioningSetRepositoryResponse] = Await.result(UpdateRepository2Async(id_repo_id, body), Duration.Inf)

}
