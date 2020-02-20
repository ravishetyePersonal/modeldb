package ai.verta.swagger.client

import net.liftweb.json.Serialization.write
import net.liftweb.json.{DefaultFormats, parse}
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client._
import sttp.model._
import java.net.URI

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class HttpClient(val host: String, val headers: Map[String, String]) extends Client {
  implicit val formats = DefaultFormats
  implicit val sttpBackend = AsyncHttpClientFutureBackend()

  def request[T1, T2](method: String, path: String, query: Map[String, String], body: T1)(implicit ec: ExecutionContext, m: Manifest[T2]): Future[Try[T2]] = {
    val request = if (body != null) basicRequest.body(write(body)) else basicRequest

    val uriPath = Uri(new URI(host + path))

    val request2 = method match {
      case "GET" => request.get(uriPath)
      case "POST" => request.post(uriPath)
      case "PUT" => request.put(uriPath)
      case "DELETE" => request.delete(uriPath)
      case other => request.get(uriPath) // TODO: exception
    }

    val futureResponse = request2.headers(headers).send()

    futureResponse.map(response => {
      response.body match {
        case Left(failureBody) => Failure(HttpException(response.code, failureBody))
        case Right(successBody) => {
          val json = parse(successBody)
          val result = json.extract[T2]
          Success(result)
        }
      }
    })
  }

  def toQuery[T](value: T): String = {
    return ""
  }

  def toQuery(value: String) = value
}