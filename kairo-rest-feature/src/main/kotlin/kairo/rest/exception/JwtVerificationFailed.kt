package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class JwtVerificationFailed(cause: Exception) : UnauthorizedException(
  message = "JWT verification failed.",
  cause = cause,
)
