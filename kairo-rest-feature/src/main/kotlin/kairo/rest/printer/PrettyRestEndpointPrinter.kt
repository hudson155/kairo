package kairo.rest.printer

import kairo.rest.template.RestEndpointPath
import kairo.rest.template.RestEndpointTemplate

/**
 * This printer is used to create human-readable versions of [RestEndpointTemplate].
 * It's used by [RestEndpointTemplate.toString].
 */
public object PrettyRestEndpointPrinter : RestEndpointPrinter() {
  override fun write(template: RestEndpointTemplate): String =
    listOfNotNull(
      "[${writeContentType(template).orEmpty()} -> ${writeAccept(template).orEmpty()}]",
      writeMethod(template),
      writePath(template),
      writeQuery(template)?.let { "($it)" },
    ).joinToString(" ")

  public fun writeContentType(template: RestEndpointTemplate): String? =
    template.contentType?.toString()

  public fun writeAccept(template: RestEndpointTemplate): String? =
    template.accept?.toString()

  public fun writeMethod(template: RestEndpointTemplate): String =
    template.method.value

  public fun writePath(template: RestEndpointTemplate): String =
    template.path.components.joinToString("/", prefix = "/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> ":${component.value}"
      }
    }

  public fun writeQuery(template: RestEndpointTemplate): String? {
    if (template.query.params.isEmpty()) return null
    return template.query.params.joinToString { (value, required) ->
      buildString {
        append(value)
        if (!required) append('?')
      }
    }
  }
}
