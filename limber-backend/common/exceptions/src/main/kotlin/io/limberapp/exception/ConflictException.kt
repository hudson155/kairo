package io.limberapp.exception

import io.ktor.http.HttpStatusCode

abstract class ConflictException(message: String) : LimberException(
    limberMessage = message,
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Conflict
}
