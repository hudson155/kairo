package kairo.rest.exception

import com.fasterxml.jackson.databind.JsonMappingException

public class InvalidProperty(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException.WithPathAndLocation("Invalid property.") {
  public companion object {
    internal fun from(e: JsonMappingException): InvalidProperty =
      InvalidProperty(
        path = parsePath(e.path),
        location = e.location?.let { parseLocation(it) },
      )
  }
}
