package kairo.rest.exception

import kairo.exception.UnauthorizedException

internal class JwtVerificationFailed(cause: Exception) : UnauthorizedException(
  message = "JWT verification failed.",
  cause = cause,
)
