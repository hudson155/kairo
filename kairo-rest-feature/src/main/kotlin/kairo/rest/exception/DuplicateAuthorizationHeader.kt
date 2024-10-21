package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class DuplicateAuthorizationHeader : UnauthorizedException(
  message = "There is more than 1 authorization header.",
)
