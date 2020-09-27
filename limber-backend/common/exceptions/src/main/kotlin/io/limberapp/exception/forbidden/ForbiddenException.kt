package io.limberapp.exception.forbidden

import io.limberapp.exception.LimberException

class ForbiddenException(cause: Exception? = null) : LimberException("Forbidden.", null, cause)
