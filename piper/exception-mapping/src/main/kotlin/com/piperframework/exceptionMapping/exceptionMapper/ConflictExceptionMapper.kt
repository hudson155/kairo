package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.exceptionMapping.PiperError
import io.ktor.http.HttpStatusCode

internal class ConflictExceptionMapper : ExceptionMapper<ConflictException> {
    override fun handle(e: ConflictException) = PiperError(HttpStatusCode.Conflict, e)
}
