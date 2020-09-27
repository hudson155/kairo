package io.limberapp.exception.notFound

import io.limberapp.exception.LimberException

abstract class NotFoundException(message: String, cause: Exception? = null) : LimberException(message, null, cause)
