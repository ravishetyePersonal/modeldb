package ai.verta.swagger.client

import sttp.model.StatusCode

final case class HttpException(private val code: StatusCode,
                               private val message: String,
                               private val cause: Throwable = None.orNull)
  extends Exception(s"[$code] $message", cause)