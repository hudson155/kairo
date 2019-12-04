package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.exception.exception.notFound.NotFoundException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.exceptionMapping.PiperError
import io.ktor.http.HttpStatusCode

internal class NotFoundExceptionMapper : ExceptionMapper<NotFoundException>() {
    override fun handle(e: NotFoundException) = PiperError(HttpStatusCode.NotFound, e)
}
