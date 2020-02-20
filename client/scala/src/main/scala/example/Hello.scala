package example

import ai.verta.swagger.client._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

object Hello extends App {
  implicit val ec = ExecutionContext.global

  val httpClient = new HttpClient("https://dev.verta.ai", Map(
    "Grpc-Metadata-email" -> sys.env("VERTA_EMAIL"),
    "Grpc-Metadata-developer_key" -> sys.env("VERTA_DEV_KEY"),
    "Grpc-Metadata-source" -> "PythonClient"
  ))
  val clientSet = new ClientSet(httpClient)
  val response = clientSet.projectService.verifyConnection()
  if (response.isSuccess) {
    println("success")
  } else {
    println(response.failed.get.getMessage)
  }

  Await.result(httpClient.sttpBackend.close(), Duration.Inf)
}
