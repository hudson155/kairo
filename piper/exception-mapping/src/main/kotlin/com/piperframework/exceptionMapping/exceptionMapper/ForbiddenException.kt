package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.forbidden.ForbiddenFrameworkError
import com.piperframework.exception.ForbiddenException
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.util.pipeline.PipelineContext

internal class ForbiddenException :
    ExceptionMapper<ForbiddenException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ForbiddenException) =
        Pair(HttpStatusCode.Forbidden, ForbiddenFrameworkError())
}
