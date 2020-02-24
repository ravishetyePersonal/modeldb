package ai.verta.swagger.client

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait Client {
  def request[T1, T2](method: String, path: String, query: Map[String, String], body: T1)(implicit ec: ExecutionContext, m: Manifest[T2]): Future[Try[T2]]

  def toQuery[T](value: T): String

  def close(): Unit
}
