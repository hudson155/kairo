package kairo.rest.template

/**
 * Part of [RestEndpointTemplate] that represents query params.
 * See the KDoc there.
 */
internal data class RestEndpointQuery(
  val params: List<Param>,
) {
  internal data class Param(
    val value: String,
    val required: Boolean,
  ) {
    private val regex: Regex = Regex("[a-z][A-Za-z0-9]*")

    init {
      require(regex.matches(value)) { "Query params must be camel case: $value." }
    }
  }

  internal companion object {
    fun of(vararg params: Param): RestEndpointQuery =
      RestEndpointQuery(params.toList())
  }
}
