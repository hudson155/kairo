package io.limberapp.common.exception.unauthorized

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

class UnauthorizedException : LimberException(
    limberMessage = "Unauthorized.",
    userVisibleProperties = emptyMap(),
    limberCause = null,
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
}
