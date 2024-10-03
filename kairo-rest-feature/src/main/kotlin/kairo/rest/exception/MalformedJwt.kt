package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MalformedJwt(cause: Exception) : UnauthorizedException(
  message = "The JWT was not well-formed.",
  cause = cause,
)
