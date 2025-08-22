package kairo.rest

/**
 * Part of [RestEndpointTemplate] that represents the path, including path params.
 * See the KDoc there.
 */
public data class RestEndpointTemplatePath(
  val components: List<Component>,
) {
  public sealed class Component {
    public data class Constant(val value: String) : Component()

    public data class Param(val value: String) : Component()

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
    public fun from(string: String): RestEndpointTemplatePath =
      RestEndpointTemplatePath(string.split('/').map { Component.from(it) })
  }
}
