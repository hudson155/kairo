package kairo.restFeature

/**
 * This writer is used to create human-readable versions of [RestEndpointTemplate].
 * It's used by [RestEndpointTemplate.toString].
 */
internal object PrettyRestEndpointWriter : RestEndpointWriter() {
  override fun write(template: RestEndpointTemplate): String =
    buildString {
      contentType(template)
      method(template)
      path(template)
      query(template)
    }

  private fun StringBuilder.contentType(template: RestEndpointTemplate) {
    append("[${template.contentType?.toString().orEmpty()} -> ${template.accept}] ")
  }

  private fun StringBuilder.method(template: RestEndpointTemplate) {
    append("${template.method.value} ")
  }

  private fun StringBuilder.path(template: RestEndpointTemplate) {
    val path = template.path.components.joinToString("/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> ":${component.value}"
      }
    }
    append(path)
  }

  private fun StringBuilder.query(template: RestEndpointTemplate) {
    if (template.query.params.isEmpty()) return
    val query = template.query.params.joinToString(", ") { param ->
      buildString {
        append(param.value)
        if (!param.required) append('?')
      }
    }
    append(" ($query)")
  }
}
