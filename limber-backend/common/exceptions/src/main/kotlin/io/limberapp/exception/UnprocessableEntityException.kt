package io.limberapp.exception

import io.ktor.http.HttpStatusCode

class UnprocessableEntityException internal constructor(
    message: String,
    userVisibleProperties: Map<String, Any>,
    cause: Exception?,
) : LimberException(
    limberMessage = message,
    userVisibleProperties = userVisibleProperties,
    limberCause = cause,
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.UnprocessableEntity
}

fun EntityNotFound.unprocessable(): UnprocessableEntityException =
    UnprocessableEntityException(limberMessage, userVisibleProperties, limberCause)
