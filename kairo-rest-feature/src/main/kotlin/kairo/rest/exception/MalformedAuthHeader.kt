package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MalformedAuthHeader : UnauthorizedException(
  message = "Malformed auth header." +
    " The HTTP auth header was not well-formed.",
)
