package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ConflictError
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class ConflictExceptionMapper : ExceptionMapper<ConflictException>() {
    override fun handle(e: ConflictException) = ConflictError()
}
