package kairo.rest.template

/**
 * Part of [RestEndpointTemplate] that represents the path, including path params.
 * See the KDoc there.
 */
public data class RestEndpointPath(
  val components: List<Component>,
) {
  public sealed class Component {
    public abstract fun validate()

    public data class Constant(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][a-z0-9]*(-[a-z][a-z0-9]*)*")

      override fun validate() {
        require(regex.matches(value)) { "Path constants must be kebab case: $value." }
      }
    }

    public data class Param(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][A-Za-z0-9]*")

      override fun validate() {
        require(regex.matches(value)) { "Path params must be camel case: $value." }
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
    public fun of(vararg components: Component): RestEndpointPath =
      RestEndpointPath(components.toList())
  }
}
