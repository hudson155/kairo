package com.piperframework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import com.piperframework.error.forbidden.ForbiddenFrameworkError
import com.piperframework.exception.ForbiddenException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class ForbiddenException :
    ExceptionMapper<ForbiddenException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ForbiddenException) {
        this.call.respond(HttpStatusCode.Forbidden, ForbiddenFrameworkError())
    }
}
