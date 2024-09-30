package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class UnknownJsonError(cause: Exception) : JsonBadRequestException(
  message = "Unknown JSON error." +
    " Something is wrong with the JSON.",
  cause = cause,
)
