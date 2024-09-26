package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException

public class UnrecognizedPolymorphicType(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException.WithPathAndLocation(
  message = "Unrecognized polymorphic type." +
    " This property could be one of several types, but the given type was not recognized.",
  ) {
  public companion object {
    internal fun from(e: MismatchedInputException): UnrecognizedPolymorphicType =
      UnrecognizedPolymorphicType(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
