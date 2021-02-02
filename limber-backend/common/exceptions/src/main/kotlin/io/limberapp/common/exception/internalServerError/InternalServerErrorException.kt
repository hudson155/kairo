package io.limberapp.common.exception.internalServerError

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

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
