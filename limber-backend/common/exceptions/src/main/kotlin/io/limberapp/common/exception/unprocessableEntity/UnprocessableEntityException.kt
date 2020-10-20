package io.limberapp.common.exception.unprocessableEntity

import io.limberapp.common.exception.LimberException
import io.limberapp.common.exception.notFound.EntityNotFound

class UnprocessableEntityException(
    message: String,
    cause: Exception? = null,
) : LimberException(message, null, cause)

fun EntityNotFound.unprocessable() = UnprocessableEntityException(message, cause)
