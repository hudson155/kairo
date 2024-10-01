package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class NoAuthorization : UnauthorizedException(
  message = "This endpoint requires authorization.",
)
