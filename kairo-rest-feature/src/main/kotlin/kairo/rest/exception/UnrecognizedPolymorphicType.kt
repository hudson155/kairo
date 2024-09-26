package kairo.rest.exception

import com.fasterxml.jackson.databind.JsonMappingException

public class UnrecognizedPolymorphicType(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException.WithPathAndLocation(
  message = "Unrecognized polymorphic type." +
    " This property could be one of several types, but the given type was not recognized.",
) {
  public companion object {
    internal fun from(e: JsonMappingException): UnrecognizedPolymorphicType =
      UnrecognizedPolymorphicType(
        path = parsePath(e.path),
        location = e.location?.let { parseLocation(it) },
      )
  }
}
