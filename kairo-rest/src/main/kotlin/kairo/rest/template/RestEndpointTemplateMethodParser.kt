package kairo.rest.template

import io.ktor.http.HttpMethod
import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

internal object RestEndpointTemplateMethodParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(kClass: KClass<out RestEndpoint<*, *>>): HttpMethod {
    val annotation = getAnnotation(kClass)
    return HttpMethod.parse(annotation.method.uppercase())
  }

  private fun getAnnotation(kClass: KClass<out RestEndpoint<*, *>>): Rest {
    val annotations = kClass.findAnnotations<Rest>()
    require(annotations.isNotEmpty()) {
      "${error.endpoint(kClass)}: Must define ${error.restAnnotation}."
    }
    return annotations.single()
  }
}
