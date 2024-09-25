package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException

public class UnrecognizedParameter(
  override val path: String?,
  override val location: Location?,
) : JacksonBadRequestException("Unrecognized parameter.") {
  public companion object {
    internal fun from(e: UnrecognizedPropertyException): UnrecognizedParameter =
      UnrecognizedParameter(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
