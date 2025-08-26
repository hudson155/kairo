package kairo.rest.template

/**
 * Part of [RestEndpointTemplate] that represents the path, including path params.
 * See the KDoc there.
 */
public data class RestEndpointTemplatePath(
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

  public sealed class Component {
    public data class Constant(val value: String) : Component() {
      init {
        require(value.isNotEmpty()) { "Constant path components cannot be empty" }
      }
    }

    public data class Param(val value: String) : Component() {
      init {
        require(value.isNotEmpty()) { "Path parameters cannot be empty" }
      }
    }

    public companion object {
      public fun from(string: String): Component =
        if (string.startsWith(':')) {
          Param(string.substring(1))
        } else {
          Constant(string)
        }
    }
  }

  public companion object {
    internal fun from(string: String): RestEndpointTemplatePath {
      require(string.startsWith('/')) { "Path must start with a slash" }
      if (string.length == 1) return RestEndpointTemplatePath(emptyList())
      return RestEndpointTemplatePath(string.drop(1).split('/').map { Component.from(it) })
    }
  }
}
