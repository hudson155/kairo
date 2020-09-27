package io.limberapp.exception.badRequest

import io.limberapp.exception.LimberException

abstract class BadRequestException(message: String, cause: Exception? = null) : LimberException(message, null, cause)
