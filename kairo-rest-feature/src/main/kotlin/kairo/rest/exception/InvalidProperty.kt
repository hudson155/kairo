package kairo.rest.exception

public class InvalidProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Invalid property.",
  cause = cause,
)
