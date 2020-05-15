package com.piperframework.exception.exception.conflict

import com.piperframework.exception.PiperException

abstract class ConflictException(
  message: String,
  developerMessage: String,
  cause: Exception? = null
) : PiperException(message, developerMessage, cause)
