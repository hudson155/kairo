package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ForbiddenError
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class ForbiddenExceptionMapper : ExceptionMapper<ForbiddenException>() {
    override fun handle(e: ForbiddenException) = ForbiddenError()
}
