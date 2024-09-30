package kairo.rest.exception

import kairo.exception.BadRequestException

internal class MalformedContentType(
  cause: Exception,
) : BadRequestException(
  message = "Malformed content type." +
    " The content type in one of the HTTP headers was not well-formed.",
  cause = cause,
)
