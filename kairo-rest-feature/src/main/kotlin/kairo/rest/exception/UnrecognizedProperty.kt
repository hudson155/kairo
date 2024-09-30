package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class UnrecognizedProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Unrecognized property." +
    " This property is not recognized. Is it named incorrectly?",
  cause = cause,
)
