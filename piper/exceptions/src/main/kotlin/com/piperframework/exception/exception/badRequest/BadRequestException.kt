package com.piperframework.exception.exception.badRequest

import com.piperframework.exception.PiperException

abstract class BadRequestException(message: String, cause: Throwable? = null) : PiperException(message, cause)
