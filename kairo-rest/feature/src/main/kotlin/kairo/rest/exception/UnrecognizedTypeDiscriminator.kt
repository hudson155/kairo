package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

internal class UnrecognizedTypeDiscriminator(
  override val cause: InvalidTypeIdException,
) : LogicalFailure("Unrecognized type discriminator", cause) {
  override val status: HttpStatusCode = HttpStatusCode.BadRequest
  override val type: String = "UnrecognizedTypeDiscriminator"

  override fun MutableMap<String, Any?>.buildJson() {
    put("path", jacksonPathReference(cause.path))
    put("discriminator", cause.typeId)
  }
}
