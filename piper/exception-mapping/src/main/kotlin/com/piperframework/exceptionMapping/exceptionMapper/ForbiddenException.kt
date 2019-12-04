package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ForbiddenError
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

internal class ForbiddenException : ExceptionMapper<ForbiddenException>() {
    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ForbiddenException) = ForbiddenError()
}
