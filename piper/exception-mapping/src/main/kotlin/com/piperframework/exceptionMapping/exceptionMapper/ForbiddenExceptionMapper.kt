package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.exceptionMapping.PiperError
import io.ktor.http.HttpStatusCode

internal class ForbiddenExceptionMapper : ExceptionMapper<ForbiddenException>() {
    override fun handle(e: ForbiddenException) = PiperError(HttpStatusCode.Forbidden)
}
