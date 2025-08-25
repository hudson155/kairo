package kairo.rest.template

import kairo.rest.KairoRouting
import kairo.rest.RestEndpoint

internal object RestEndpointTemplateErrorBuilder {
  val methodAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Method::class.simpleName}"

  val pathAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Path::class.simpleName}"

  val contentTypeAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.ContentType::class.simpleName}"

  val acceptAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Accept::class.simpleName}"

  context(routing: KairoRouting<*>)
  fun endpoint(): String =
    "REST endpoint ${routing.endpoint.kotlinClass.qualifiedName}"
}
