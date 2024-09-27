package kairo.rest.exception

import kairo.exception.BadRequestException

public class MalformedContentType : BadRequestException(
  message = "Malformed content type." +
    " The content type in one of the HTTP headers was not well-formed.",
)
