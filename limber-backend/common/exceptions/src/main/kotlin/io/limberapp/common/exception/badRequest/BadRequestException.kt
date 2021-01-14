package io.limberapp.common.exception.badRequest

import io.limberapp.common.exception.LimberException

abstract class BadRequestException(
    message: String,
    cause: Exception? = null,
) : LimberException(message, null, cause)
