package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MalformedJwt : UnauthorizedException(
  message = "The JWT was not well-formed.",
)
