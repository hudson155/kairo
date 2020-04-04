package com.piperframework.exception.exception.badRequest

import com.piperframework.exception.PiperException

abstract class BadRequestException(message: String, cause: Exception? = null) : PiperException(message, null, cause)
