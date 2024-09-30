package kairo.rest.exception

import kairo.exception.UnsupportedMediaTypeException
import kairo.rest.server.installStatusPages

/**
 * Thrown from [installStatusPages].
 * Not intended to be thrown externally.
 */
public class ContentTypeMismatch : UnsupportedMediaTypeException(
  message = "This endpoint does not support the provided Content-Type header.",
)
