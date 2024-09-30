package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class InvalidProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Invalid property.",
  cause = cause,
)
