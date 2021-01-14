package io.limberapp.common.exception.notFound

import io.limberapp.common.exception.LimberException

abstract class NotFoundException(
    message: String,
    cause: Exception? = null,
) : LimberException(message, null, cause)
