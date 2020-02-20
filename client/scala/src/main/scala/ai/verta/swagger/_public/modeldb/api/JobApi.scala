
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import ai.verta.swagger._public.modeldb.model._

class JobApi(client: Client, val basePath: String = "/v1") {

  def createJobAsync(body: ModeldbCreateJob)(implicit ec: ExecutionContext): Future[Try[ModeldbCreateJobResponse]] = {
    val __query = Map[String,String](
      
    )
    if (body == null) throw new Exception("Missing required parameter \"body\"")
    return client.request[ModeldbCreateJob, ModeldbCreateJobResponse]("POST", basePath + s"/job/createJob", __query, body)
  }

  def createJob(body: ModeldbCreateJob)(implicit ec: ExecutionContext): Try[ModeldbCreateJobResponse] = Await.result(createJobAsync(body), Duration.Inf)


  def deleteJobAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbDeleteJobResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbDeleteJobResponse]("GET", basePath + s"/job/deleteJob", __query, body)
  }

  def deleteJob(id: String)(implicit ec: ExecutionContext): Try[ModeldbDeleteJobResponse] = Await.result(deleteJobAsync(id), Duration.Inf)


  def getJobAsync(id: String)(implicit ec: ExecutionContext): Future[Try[ModeldbGetJobResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id)
    )
    val body: Any = null
    return client.request[Any, ModeldbGetJobResponse]("GET", basePath + s"/job/getJob", __query, body)
  }

  def getJob(id: String)(implicit ec: ExecutionContext): Try[ModeldbGetJobResponse] = Await.result(getJobAsync(id), Duration.Inf)


  def updateJobAsync(id: String, endTime: String, jobStatus: String)(implicit ec: ExecutionContext): Future[Try[ModeldbUpdateJobResponse]] = {
    val __query = Map[String,String](
      "id" -> client.toQuery(id),
      "end_time" -> client.toQuery(endTime),
      "job_status" -> client.toQuery(jobStatus)
    )
    val body: Any = null
    return client.request[Any, ModeldbUpdateJobResponse]("GET", basePath + s"/job/updateJob", __query, body)
  }

  def updateJob(id: String, endTime: String, jobStatus: String)(implicit ec: ExecutionContext): Try[ModeldbUpdateJobResponse] = Await.result(updateJobAsync(id, endTime, jobStatus), Duration.Inf)

}
