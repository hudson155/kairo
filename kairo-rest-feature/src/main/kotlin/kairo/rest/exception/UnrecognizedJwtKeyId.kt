package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class UnrecognizedJwtKeyId : UnauthorizedException(
  message = "Unrecognized JWT key ID." +
    " The key ID on the JWT is not recognized.",
)
