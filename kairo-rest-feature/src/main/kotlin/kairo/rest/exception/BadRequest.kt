package kairo.rest.exception

import kairo.exception.BadRequestException
import kairo.rest.exceptionHandler.KairoHandler

/**
 * Generic bad request exception. Use a more specific type whenever possible.
 * Thrown from [KairoHandler].
 * Not intended to be thrown externally.
 */
public class BadRequest(cause: Exception) : BadRequestException(null, cause)
