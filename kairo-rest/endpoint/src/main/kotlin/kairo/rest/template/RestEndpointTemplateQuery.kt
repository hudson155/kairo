package kairo.rest.template

/**
 * Part of [RestEndpointTemplate] that represents query params.
 * See the KDoc there.
 */
public data class RestEndpointTemplateQuery(
  val params: List<Param>,
) {
  public constructor(vararg params: Param) : this(params.toList())

  override fun toString(): String =
    params.joinToString(prefix = "(", postfix = ")") { (value, required) ->
      buildString {
        append(value)
        if (!required) append('?')
      }
    }

  public data class Param(
    val value: String,
    val required: Boolean,
  )
}
