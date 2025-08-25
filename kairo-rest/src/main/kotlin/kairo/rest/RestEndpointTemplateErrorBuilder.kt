package kairo.rest

internal object RestEndpointTemplateErrorBuilder {
  val definition: String =
    "@${RestEndpoint::class.simpleName}.${RestEndpoint.Definition::class.simpleName}"

  context(routing: KairoRouting<*>)
  fun endpoint(): String =
    "REST endpoint ${routing.endpoint.kotlinClass.qualifiedName}"
}
