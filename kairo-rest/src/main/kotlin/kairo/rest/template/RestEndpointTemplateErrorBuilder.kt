package kairo.rest.template

import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass

internal object RestEndpointTemplateErrorBuilder {
  val restAnnotation: String =
    "@${Rest::class.simpleName}"

  val pathParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.PathParam::class.simpleName}"

  val queryParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.QueryParam::class.simpleName}"

  val contentTypeAnnotation: String =
    "@${Rest::class.simpleName}.${Rest.ContentType::class.simpleName}"

  val acceptAnnotation: String =
    "@${Rest::class.simpleName}.${Rest.Accept::class.simpleName}"

  fun endpoint(endpoint: KClass<out RestEndpoint<*, *>>): String =
    "REST endpoint ${endpoint.qualifiedName}"
}
