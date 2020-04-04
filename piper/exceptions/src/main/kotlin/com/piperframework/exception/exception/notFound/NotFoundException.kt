package com.piperframework.exception.exception.notFound

import com.piperframework.exception.PiperException

abstract class NotFoundException(message: String, cause: Exception? = null) : PiperException(message, null, cause)
