package kairo.rest

/**
 * Part of [RestEndpointTemplate] that represents the path, including path params.
 * See the KDoc there.
 */
internal data class RestEndpointPath(
  val components: List<Component>,
) {
  internal sealed class Component {
    internal data class Constant(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][a-z0-9]*(-[a-z][a-z0-9]*)*")

      override fun validate() {
        require(regex.matches(value)) { "Path constants must be kebab case: $value." }
      }
    }

    internal data class Param(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][A-Za-z0-9]*")

      override fun validate() {
        require(regex.matches(value)) { "Path params must be camel case: $value." }
      }
    }

    abstract fun validate()

    internal companion object {
      fun from(string: String): Component =
        if (string.startsWith(':')) {
          Param(string.substring(1))
        } else {
          Constant(string)
        }
    }
  }

  internal companion object {
    fun of(vararg components: Component): RestEndpointPath =
      RestEndpointPath(components.toList())
  }
}
