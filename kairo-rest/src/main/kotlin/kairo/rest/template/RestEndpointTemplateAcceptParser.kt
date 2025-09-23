package kairo.rest.template

import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

internal object RestEndpointTemplateAcceptParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(endpoint: KClass<out RestEndpoint<*, *>>): ContentType? {
    val annotation = getAnnotation(endpoint) ?: return null
    wrapErrorMessage(endpoint) {
      return ContentType.parse(annotation.value)
    }
  }

  private fun getAnnotation(endpoint: KClass<out RestEndpoint<*, *>>): Rest.Accept? {
    val annotations = endpoint.findAnnotations<Rest.Accept>()
    if (annotations.isEmpty()) return null
    val annotation = annotations.singleOrNull()
    requireNotNull(annotation) {
      "Endpoint ${endpoint.qualifiedName} cannot define multiple of ${error.restAnnotation}."
    }
    return annotation
  }

  private inline fun <T> wrapErrorMessage(
    endpoint: KClass<out RestEndpoint<*, *>>,
    block: () -> T,
  ): T {
    try {
      return block()
    } catch (e: BadContentTypeFormatException) {
      val message = buildString {
        append("${error.endpoint(endpoint)}: ${error.acceptAnnotation} is invalid.")
        e.message?.let { append(" $it.") }
      }
      throw IllegalArgumentException(message, e)
    }
  }
}
