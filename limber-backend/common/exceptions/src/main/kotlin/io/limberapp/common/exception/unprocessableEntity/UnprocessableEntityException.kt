package io.limberapp.common.exception.unprocessableEntity

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.LimberException
import io.limberapp.common.exception.notFound.EntityNotFound

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
