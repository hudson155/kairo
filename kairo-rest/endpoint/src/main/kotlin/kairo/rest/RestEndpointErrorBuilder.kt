package kairo.rest

import kotlin.reflect.KClass

public object RestEndpointErrorBuilder {
  public val restAnnotation: String =
    "@${Rest::class.simpleName}"

  public val pathParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.PathParam::class.simpleName}"

  public val queryParamAnnotation: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.QueryParam::class.simpleName}"

  public val contentTypeAnnotation: String =
    "@${Rest::class.simpleName}.${Rest.ContentType::class.simpleName}"

  public val acceptAnnotation: String =
    "@${Rest::class.simpleName}.${Rest.Accept::class.simpleName}"

  public fun endpoint(kClass: KClass<out RestEndpoint<*, *>>): String =
    "REST endpoint ${kClass.qualifiedName}"
}
