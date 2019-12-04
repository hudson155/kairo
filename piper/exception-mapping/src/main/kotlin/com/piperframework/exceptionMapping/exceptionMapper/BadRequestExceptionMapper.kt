package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.exceptionMapping.PiperError
import io.ktor.http.HttpStatusCode

internal class BadRequestExceptionMapper : ExceptionMapper<BadRequestException>() {
    override fun handle(e: BadRequestException) = PiperError(HttpStatusCode.BadRequest)
}
