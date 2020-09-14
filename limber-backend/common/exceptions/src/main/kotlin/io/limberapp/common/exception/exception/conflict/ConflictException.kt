package io.limberapp.common.exception.exception.conflict

import io.limberapp.common.exception.LimberException

abstract class ConflictException(
  message: String,
  developerMessage: String,
  cause: Exception? = null,
) : LimberException(message, developerMessage, cause)
