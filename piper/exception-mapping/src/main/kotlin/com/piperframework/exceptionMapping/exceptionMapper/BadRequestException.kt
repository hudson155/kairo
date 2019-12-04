package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.BadRequestError
import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

internal class BadRequestException : ExceptionMapper<BadRequestException>() {
    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: BadRequestException) = BadRequestError()
}
