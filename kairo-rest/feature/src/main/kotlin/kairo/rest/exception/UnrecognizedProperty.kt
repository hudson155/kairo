package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.PropertyBindingException
import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

internal class UnrecognizedProperty(
  override val cause: PropertyBindingException,
) : LogicalFailure("Unrecognized property", cause) {
  override val status: HttpStatusCode = HttpStatusCode.BadRequest
  override val type: String = "UnrecognizedProperty"

  override fun MutableMap<String, Any?>.buildJson() {
    put("path", jacksonPathReference(cause.path))
  }
}
