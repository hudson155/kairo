package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.InvalidNullException
import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

internal class MissingProperty(
  override val cause: InvalidNullException,
) : LogicalFailure("Missing property", cause) {
  override val status: HttpStatusCode = HttpStatusCode.BadRequest
  override val type: String = "MissingProperty"

  override fun MutableMap<String, Any?>.buildJson() {
    put("path", jacksonPathReference(cause.path))
  }
}
