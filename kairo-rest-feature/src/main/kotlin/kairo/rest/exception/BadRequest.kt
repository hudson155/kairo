package kairo.rest.exception

import kairo.exception.BadRequestException

/**
 * Generic bad request exception. Use a more specific type whenever possible.
 */
public class BadRequest(cause: Exception) : BadRequestException(null, cause)
