package kairo.rest.template

import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kairo.rest.RestEndpointErrorBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

internal object RestEndpointTemplateAcceptParser {
  private val error: RestEndpointErrorBuilder = RestEndpointErrorBuilder

  fun parse(kClass: KClass<out RestEndpoint<*, *>>): ContentType? {
    val annotation = getAnnotation(kClass) ?: return null
    wrapErrorMessage(kClass) {
      return ContentType.parse(annotation.value)
    }
  }

  private fun getAnnotation(kClass: KClass<out RestEndpoint<*, *>>): Rest.Accept? {
    val annotations = kClass.findAnnotations<Rest.Accept>()
    return annotations.singleNullOrThrow()
  }

  private inline fun <T> wrapErrorMessage(
    kClass: KClass<out RestEndpoint<*, *>>,
    block: () -> T,
  ): T {
    try {
      return block()
    } catch (e: BadContentTypeFormatException) {
      val message = buildString {
        append("${error.endpoint(kClass)}: ${error.acceptAnnotation} is invalid.")
        e.message?.let { append(" $it.") }
      }
      throw IllegalArgumentException(message, e)
    }
  }
}
