package io.limberapp.exception

import io.ktor.http.HttpStatusCode

class UnauthorizedException : LimberException(
    limberMessage = "Unauthorized.",
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
}
