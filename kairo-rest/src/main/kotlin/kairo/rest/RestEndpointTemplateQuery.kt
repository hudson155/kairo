package kairo.rest

/**
 * Part of [RestEndpointTemplate] that represents query params.
 * See the KDoc there.
 */
public data class RestEndpointTemplateQuery(
  val params: List<Param>,
) {
  public data class Param(
    val value: String,
    val required: Boolean,
  )
}
