package io.limberapp.common.exception.badRequest

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

abstract class BadRequestException(
    message: String,
    userVisibleProperties: Map<String, Any> = emptyMap(),
    cause: Exception? = null,
) : LimberException(
    limberMessage = message,
    userVisibleProperties = userVisibleProperties,
    limberCause = cause,
) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
}
