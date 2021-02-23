package io.limberapp.exception

import io.ktor.http.HttpStatusCode

class ForbiddenException : LimberException(
    limberMessage = "Forbidden.",
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
}
