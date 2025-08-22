package kairo.rest.endpoint

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.rest.template.RestEndpointTemplate

/**
 * Represents an instance of [RestEndpoint],
 * conflating some metadata of [RestEndpointTemplate].
 */
public data class RestEndpointDetails(
  val method: HttpMethod,
  val path: String,
  val contentType: ContentType?,
  val accept: ContentType?,
  val body: Any?,
)
