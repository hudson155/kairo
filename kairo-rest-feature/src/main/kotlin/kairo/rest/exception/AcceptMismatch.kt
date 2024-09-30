package kairo.rest.exception

import kairo.exception.NotAcceptableException

internal class AcceptMismatch : NotAcceptableException(
  message = "This endpoint does not support the provided Accept header.",
)
