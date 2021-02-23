package io.limberapp.exception

import io.ktor.http.HttpStatusCode

abstract class InternalServerErrorException(
    message: String,
    cause: Exception? = null,
) : LimberException(
    limberMessage = message,
    userVisibleProperties = emptyMap(),
    limberCause = cause,
) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError
}
