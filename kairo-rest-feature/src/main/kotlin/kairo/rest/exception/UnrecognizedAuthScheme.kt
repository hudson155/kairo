package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class UnrecognizedAuthScheme : UnauthorizedException(
  message = "Unrecognized auth scheme." +
    " This auth scheme is not recognized.",
)
