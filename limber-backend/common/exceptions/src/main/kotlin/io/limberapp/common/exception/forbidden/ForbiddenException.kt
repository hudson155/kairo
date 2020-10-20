package io.limberapp.common.exception.forbidden

import io.limberapp.common.exception.LimberException

class ForbiddenException(cause: Exception? = null) : LimberException("Forbidden.", null, cause)
