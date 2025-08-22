package kairo.rest.exception

import kairo.exception.NotAcceptableException

public class AcceptMismatch : NotAcceptableException(
  message = "This endpoint does not support the provided Accept header.",
)
