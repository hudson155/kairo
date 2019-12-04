package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.BadRequestError
import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class BadRequestExceptionMapper : ExceptionMapper<BadRequestException>() {
    override fun handle(e: BadRequestException) = BadRequestError()
}
