package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class MalformedJson(
  cause: Exception,
) : JsonBadRequestException(
  message = "Malformed JSON." +
    " The JSON provided was not well-formed.",
  cause = cause,
)
