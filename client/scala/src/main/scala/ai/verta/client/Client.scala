package ai.verta.client

import ai.verta.client.entities.{GetOrCreateEntity, Project}
import ai.verta.swagger._public.modeldb.model.ModeldbCreateProject
import ai.verta.swagger.client.{ClientSet, HttpClient}

import scala.concurrent.ExecutionContext

class Client(conn: ClientConnection) {
  val httpClient = new HttpClient(conn.host, Map(
    "Grpc-Metadata-email" -> conn.auth.email,
    "Grpc-Metadata-developer_key" -> conn.auth.devKey,
    "Grpc-Metadata-source" -> "PythonClient"
  ))

  val clientSet = new ClientSet(httpClient)

  def close() = httpClient.close()

  def getOrCreateProject(name: String, workspace: String = "")(implicit ec: ExecutionContext) = {
    GetOrCreateEntity.getOrCreate[Project](
      get = () => {
        clientSet.projectService.getProjectByName(name = name, workspace_name = workspace)
          .map(r => if (r.project_by_user.isEmpty) null else new Project(clientSet, r.project_by_user.get))
      },
      create = () => {
        clientSet.projectService.createProject(ModeldbCreateProject(name = Some(name), workspace_name = Some(workspace)))
          .map(r => if (r.project.isEmpty) null else new Project(clientSet, r.project.get))
      })
  }
}
