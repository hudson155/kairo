package kairo.rest.template

import io.ktor.http.HttpMethod
import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

internal object RestEndpointTemplateMethodParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(endpoint: KClass<out RestEndpoint<*, *>>): HttpMethod {
    val annotation = getAnnotation(endpoint)
    return HttpMethod.parse(annotation.method.uppercase())
  }

  private fun getAnnotation(endpoint: KClass<out RestEndpoint<*, *>>): Rest {
    val annotations = endpoint.findAnnotations<Rest>()
    require(annotations.isNotEmpty()) {
      "${error.endpoint(endpoint)}: Must define ${error.restAnnotation}."
    }
    val annotation = annotations.singleOrNull()
    requireNotNull(annotation) {
      "${error.endpoint(endpoint)}: Cannot define multiple of ${error.restAnnotation}."
    }
    return annotation
  }
}
