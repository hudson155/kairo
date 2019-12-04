package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ConflictError
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class ConflictException : ExceptionMapper<ConflictException>() {
    override fun handle(e: ConflictException) = ConflictError()
}
