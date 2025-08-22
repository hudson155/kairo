package kairo.rest

import kotlin.reflect.KClass

internal object RestEndpointTemplateErrorBuilder {
  val methodAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Method::class.simpleName}"

  val pathAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Path::class.simpleName}"

  val contentTypeAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.ContentType::class.simpleName}"

  val acceptAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Accept::class.simpleName}"

  fun restEndpoint(endpoint: KClass<out RestEndpoint<*, *>>): String =
    "REST endpoint ${endpoint.qualifiedName}"
}
