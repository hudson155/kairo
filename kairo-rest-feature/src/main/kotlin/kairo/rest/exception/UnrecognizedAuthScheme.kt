package kairo.rest.exception

import kairo.exception.UnauthorizedException

internal class UnrecognizedAuthScheme : UnauthorizedException(
  message = "Unrecognized auth scheme." +
    " This auth scheme is not recognized.",
)
