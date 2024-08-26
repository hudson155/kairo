package kairo.rest

/**
 * This writer is used by Ktor when routing REST endpoints.
 * Only the path is included; other aspects are routed separately.
 */
internal object KtorPathTemplateRestEndpointWriter : RestEndpointWriter() {
  override fun write(template: RestEndpointTemplate): String =
    buildString {
      path(template)
    }

  private fun StringBuilder.path(template: RestEndpointTemplate) {
    val path = template.path.components.joinToString("/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> "{${component.value}}"
      }
    }
    append(path)
  }
}
