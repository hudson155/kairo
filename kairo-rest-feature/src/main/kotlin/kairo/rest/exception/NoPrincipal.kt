package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class NoPrincipal : UnauthorizedException(
  message = "No authorization principal was provided.",
)
