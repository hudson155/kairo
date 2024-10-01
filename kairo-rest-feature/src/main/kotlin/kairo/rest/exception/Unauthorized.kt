package kairo.rest.exception

import kairo.exception.UnauthorizedException

/**
 * Generic unauthorized exception. Use a more specific type whenever possible.
 */
internal class Unauthorized(cause: Exception) : UnauthorizedException(null, cause)
