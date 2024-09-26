package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException

public class WrongParameterType(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException("Wrong parameter type.") {
  public companion object {
    internal fun from(e: MismatchedInputException): WrongParameterType =
      WrongParameterType(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
