package kairo.rest.template

import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

internal object RestEndpointTemplateContentTypeParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(kClass: KClass<out RestEndpoint<*, *>>): ContentType? {
    val annotation = getAnnotation(kClass) ?: return null
    wrapErrorMessage(kClass) {
      return ContentType.parse(annotation.value)
    }
  }

  private fun getAnnotation(kClass: KClass<out RestEndpoint<*, *>>): Rest.ContentType? {
    val annotations = kClass.findAnnotations<Rest.ContentType>()
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
        append("${error.endpoint(kClass)}: ${error.contentTypeAnnotation} is invalid.")
        e.message?.let { append(" $it.") }
      }
      throw IllegalArgumentException(message, e)
    }
  }
}
