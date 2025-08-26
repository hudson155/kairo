package kairo.rest.template

import kairo.rest.RestEndpoint
import kotlin.reflect.KClass

internal object RestEndpointTemplateErrorBuilder {
  val methodAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Method::class.simpleName}"

  val pathAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Path::class.simpleName}"

  val pathParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.PathParam::class.simpleName}"

  val queryParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.QueryParam::class.simpleName}"

  val contentTypeAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.ContentType::class.simpleName}"

  val acceptAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Accept::class.simpleName}"

  fun endpoint(endpoint: KClass<out RestEndpoint<*, *>>): String =
    "REST endpoint ${endpoint.qualifiedName}"
}
