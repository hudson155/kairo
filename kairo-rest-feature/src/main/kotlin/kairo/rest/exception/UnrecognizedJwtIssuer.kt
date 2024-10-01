package kairo.rest.exception

import kairo.exception.UnauthorizedException

internal class UnrecognizedJwtIssuer : UnauthorizedException(
  message = "Unrecognized JWT issuer." +
    " This JWT issuer is not recognized.",
)
