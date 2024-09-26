package kairo.rest.exception

import com.fasterxml.jackson.databind.JsonMappingException

public class UnrecognizedProperty(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException.WithPathAndLocation(
  message = "Unrecognized property." +
    " This property is not recognized. Is it named incorrectly?",
) {
  public companion object {
    internal fun from(e: JsonMappingException): UnrecognizedProperty =
      UnrecognizedProperty(
        path = parsePath(e.path),
        location = e.location?.let { parseLocation(it) },
      )
  }
}
