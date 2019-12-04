package com.piperframework.exception.exception.forbidden

import com.piperframework.exception.PiperException

abstract class ForbiddenException(message: String, cause: Throwable? = null) : PiperException(message, cause)
