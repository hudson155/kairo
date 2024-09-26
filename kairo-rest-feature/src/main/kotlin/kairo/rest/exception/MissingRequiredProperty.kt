package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException

public class MissingRequiredProperty(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException.WithPathAndLocation(
  message = "Missing required property." +
    " This property is required, but was not provided or was null.",
) {
  public companion object {
    internal fun from(e: MismatchedInputException): MissingRequiredProperty =
      MissingRequiredProperty(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
