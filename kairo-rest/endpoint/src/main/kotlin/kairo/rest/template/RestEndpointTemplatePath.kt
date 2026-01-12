package kairo.rest.template

/**
 * Part of [RestEndpointTemplate] that represents the path, including path params.
 * See the KDoc there.
 */
internal data class RestEndpointTemplatePath(
  val components: List<Component>,
) {
  internal constructor(vararg components: Component) : this(components.toList())

  override fun toString(): String =
    components.joinToString("/", prefix = "/") { component ->
      when (component) {
        is Component.Constant -> component.value
        is Component.Param -> ":${component.value}"
      }
    }

  internal sealed class Component {
    internal data class Constant(val value: String) : Component() {
      init {
        require(value.isNotEmpty())
      }
    }

    internal data class Param(val value: String) : Component() {
      init {
        require(value.isNotEmpty())
      }
    }

    internal companion object {
      fun from(string: String): Component =
        if (string.startsWith(':')) {
          Param(string.substring(1))
        } else {
          Constant(string)
        }
    }
  }
}
