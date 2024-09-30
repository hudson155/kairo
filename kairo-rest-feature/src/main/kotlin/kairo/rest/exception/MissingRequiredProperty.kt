package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class MissingRequiredProperty(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Missing required property." +
    " This property is required, but was not provided or was null.",
  cause = cause,
)
