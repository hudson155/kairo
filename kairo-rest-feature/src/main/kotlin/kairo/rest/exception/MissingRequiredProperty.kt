package kairo.rest.exception

public class MissingRequiredProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Missing required property." +
    " This property is required, but was not provided or was null.",
  cause = cause,
)
