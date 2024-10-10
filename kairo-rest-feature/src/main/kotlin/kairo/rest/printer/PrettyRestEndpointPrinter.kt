package kairo.rest.printer

import kairo.rest.template.RestEndpointPath
import kairo.rest.template.RestEndpointTemplate

/**
 * This printer is used to create human-readable versions of [RestEndpointTemplate].
 * It's used by [RestEndpointTemplate.toString].
 */
internal object PrettyRestEndpointPrinter : RestEndpointPrinter() {
  override fun write(template: RestEndpointTemplate): String =
    listOfNotNull(
      writeContentType(template),
      writeMethod(template),
      writePath(template),
      writeQuery(template),
    ).joinToString(" ")

  fun writeContentType(template: RestEndpointTemplate): String =
    "[${template.contentType?.toString().orEmpty()} -> ${template.accept?.toString().orEmpty()}]"

  fun writeMethod(template: RestEndpointTemplate): String =
    template.method.value

  fun writePath(template: RestEndpointTemplate): String {
    val path = template.path.components.joinToString("/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> ":${component.value}"
      }
    }
    return "/$path"
  }

  fun writeQuery(template: RestEndpointTemplate): String? {
    if (template.query.params.isEmpty()) return null
    val query = template.query.params.joinToString { (value, required) ->
      buildString {
        append(value)
        if (!required) append('?')
      }
    }
    return "($query)"
  }
}
