package io.limberapp.common.exception.notFound

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException

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
