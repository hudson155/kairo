package kairo.rest.printer

import kairo.rest.template.RestEndpointPath
import kairo.rest.template.RestEndpointTemplate

/**
 * This printer is used by Ktor when routing REST endpoints.
 * Only the path is included; other aspects are routed separately.
 */
public object KtorPathTemplateRestEndpointPrinter : RestEndpointPrinter() {
  override fun write(template: RestEndpointTemplate): String =
    buildString {
      path(template)
    }

  private fun StringBuilder.path(template: RestEndpointTemplate) {
    val path = template.path.components.joinToString("/", prefix = "/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> "{${component.value}}"
      }
    }
    append(path)
  }
}
