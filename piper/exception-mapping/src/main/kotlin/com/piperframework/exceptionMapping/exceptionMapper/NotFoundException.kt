package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.notFound.NotFoundFrameworkError
import com.piperframework.exception.NotFoundException
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

internal class NotFoundException :
    ExceptionMapper<NotFoundException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: NotFoundException) {
        this.call.respond(HttpStatusCode.NotFound, NotFoundFrameworkError())
    }
}
