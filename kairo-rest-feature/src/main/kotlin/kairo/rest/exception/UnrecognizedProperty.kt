package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException

public class UnrecognizedProperty(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException.WithPathAndLocation(
  message = "Unrecognized property." +
    " This property is not recognized. Is it named incorrectly?",
  ) {
  public companion object {
    internal fun from(e: MismatchedInputException): UnrecognizedProperty =
      UnrecognizedProperty(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
