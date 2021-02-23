package io.limberapp.exception

import io.ktor.http.HttpStatusCode

abstract class NotFoundException(
    message: String,
    userVisibleProperties: Map<String, Any> = emptyMap(),
) : LimberException(
    limberMessage = message,
    userVisibleProperties = userVisibleProperties,
    limberCause = null,
) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.NotFound
}
