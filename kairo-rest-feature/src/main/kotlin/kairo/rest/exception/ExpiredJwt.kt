package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class ExpiredJwt(cause: Exception) : UnauthorizedException(
  message = "The JWT is expired.",
  cause = cause,
)
