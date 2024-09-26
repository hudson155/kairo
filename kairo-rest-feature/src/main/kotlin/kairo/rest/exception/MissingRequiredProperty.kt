package kairo.rest.exception

import com.fasterxml.jackson.databind.JsonMappingException

public class MissingRequiredProperty(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException.WithPathAndLocation(
  message = "Missing required property." +
    " This property is required, but was not provided or was null.",
) {
  public companion object {
    internal fun from(e: JsonMappingException): MissingRequiredProperty =
      MissingRequiredProperty(
        path = parsePath(e.path),
        location = e.location?.let { parseLocation(it) },
      )
  }
}
