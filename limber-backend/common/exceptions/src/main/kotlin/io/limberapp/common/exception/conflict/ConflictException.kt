package io.limberapp.common.exception.conflict

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

abstract class ConflictException(message: String) : LimberException(
    limberMessage = message,
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Conflict
}
