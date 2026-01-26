package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

internal class InvalidProperty(
  override val cause: MismatchedInputException,
) : LogicalFailure("Invalid property", cause) {
  override val status: HttpStatusCode = HttpStatusCode.BadRequest
  override val type: String = "InvalidProperty"

  override fun MutableMap<String, Any?>.buildJson() {
    put("path", jacksonPathReference(cause.path))
  }
}
