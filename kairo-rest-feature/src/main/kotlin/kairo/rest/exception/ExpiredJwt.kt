package kairo.rest.exception

import kairo.exception.UnauthorizedException

internal class ExpiredJwt(cause: Exception) : UnauthorizedException(
  message = "The JWT is expired.",
  cause = cause,
)
