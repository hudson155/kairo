package kairo.rest.exception

import kairo.exception.BadRequestException
import kairo.rest.exceptionHandler.KtorHandler

/**
 * Thrown from [KtorHandler]
 * Not intended to be thrown externally.
 */
public class MalformedContentType(
  cause: Exception,
) : BadRequestException(
  message = "Malformed content type." +
    " The content type in one of the HTTP headers was not well-formed.",
  cause = cause,
)
