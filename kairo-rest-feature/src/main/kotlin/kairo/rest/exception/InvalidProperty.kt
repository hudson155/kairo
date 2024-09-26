package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException

public class InvalidProperty(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException.WithPathAndLocation("Invalid property.") {
  public companion object {
    internal fun from(e: MismatchedInputException): InvalidProperty =
      InvalidProperty(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
