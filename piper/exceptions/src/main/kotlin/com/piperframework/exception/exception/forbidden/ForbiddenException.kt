package com.piperframework.exception.exception.forbidden

import com.piperframework.exception.PiperException

class ForbiddenException(cause: Throwable? = null) : PiperException("Forbidden", cause)
