package com.piperframework.exception.exception.forbidden

import com.piperframework.exception.PiperException

class ForbiddenException(cause: Exception? = null) : PiperException("Forbidden.", null, cause)
