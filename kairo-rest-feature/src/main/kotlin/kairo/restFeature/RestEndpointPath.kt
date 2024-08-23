package kairo.restFeature

internal data class RestEndpointPath(
  val components: List<Component>,
) {
  internal sealed class Component {
    internal data class Constant(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][a-z0-9]*(-[a-z][a-z0-9]*)*")

      init {
        require(regex.matches(value)) { "Path constants must be kebab case: $value." }
      }
    }

    internal data class Param(val value: String) : Component() {
      private val regex: Regex = Regex("[a-z][A-Za-z0-9]*")

      init {
        require(regex.matches(value)) { "Path params must be camel case: $value." }
      }
    }

    internal companion object {
      fun from(value: String): Component =
        if (value.startsWith(':')) {
          Param(value.substring(1))
        } else {
          Constant(value)
        }
    }
  }

  internal companion object {
    fun of(vararg components: Component): RestEndpointPath =
      RestEndpointPath(components.toList())
  }
}
