package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException

abstract class ExceptionMapper<T : PiperException> {
    abstract fun handle(e: T): PiperError
}
