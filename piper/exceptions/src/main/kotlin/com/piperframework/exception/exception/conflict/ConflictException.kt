package com.piperframework.exception.exception.conflict

import com.piperframework.exception.PiperException

abstract class ConflictException(message: String, cause: Throwable? = null) : PiperException(message, cause)
