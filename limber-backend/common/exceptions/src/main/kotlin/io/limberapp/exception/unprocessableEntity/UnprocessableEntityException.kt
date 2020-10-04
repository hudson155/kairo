package io.limberapp.exception.unprocessableEntity

import io.limberapp.exception.LimberException
import io.limberapp.exception.notFound.EntityNotFound

open class UnprocessableEntityException(
  message: String,
  cause: Exception? = null,
) : LimberException(message, null, cause)

fun EntityNotFound.unprocessable() = UnprocessableEntityException(message, cause)
