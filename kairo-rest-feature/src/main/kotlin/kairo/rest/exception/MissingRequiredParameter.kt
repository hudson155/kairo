@file:Suppress("DEPRECATION")

package kairo.rest.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException

public class MissingRequiredParameter(
  override val path: String?,
  override val location: Location,
) : JacksonBadRequestException("Missing required parameter.") {
  public companion object {
    internal fun from(e: MissingKotlinParameterException): MissingRequiredParameter =
      MissingRequiredParameter(
        path = parsePath(e.path),
        location = parseLocation(e.location),
      )
  }
}
