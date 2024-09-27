package kairo.rest.exception

public class InvalidProperty(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException("Invalid property.")
