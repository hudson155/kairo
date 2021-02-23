package io.limberapp.exception

import io.ktor.http.HttpStatusCode

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
