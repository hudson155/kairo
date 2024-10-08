package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MissingJwtClaim(name: String) : UnauthorizedException(
  message = "The $name claim is missing from the JWT",
)
