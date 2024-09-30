package kairo.rest.exception

import kairo.exception.NotAcceptableException
import kairo.rest.server.installStatusPages

/**
 * Thrown from [installStatusPages].
 * Not intended to be thrown externally.
 */
public class AcceptMismatch : NotAcceptableException(
  message = "This endpoint does not support the provided Accept header.",
)
