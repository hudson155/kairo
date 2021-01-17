package io.limberapp.common.exception.forbidden

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

class ForbiddenException : LimberException(
    limberMessage = "Forbidden.",
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
}
