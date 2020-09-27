package io.limberapp.exception.conflict

import io.limberapp.exception.LimberException

abstract class ConflictException(
  message: String,
  developerMessage: String,
  cause: Exception? = null,
) : LimberException(message, developerMessage, cause)
