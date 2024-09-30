package kairo.rest.exception

import kairo.exception.NotFoundException
import kairo.rest.server.installStatusPages

/**
 * Thrown from [installStatusPages].
 * Not intended to be thrown externally.
 */
public class EndpointNotFound : NotFoundException(
  message = "This endpoint does not exist." +
    " Check the method, path, and query params.",
)
