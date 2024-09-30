package kairo.rest.exception

internal class InvalidProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Invalid property.",
  cause = cause,
)
