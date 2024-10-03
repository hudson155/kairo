package kairo.rest.exception

import kairo.exception.UnauthorizedException

internal class DuplicateAuthorizationHeader : UnauthorizedException(
  message = "There is more than 1 authorization header",
)
