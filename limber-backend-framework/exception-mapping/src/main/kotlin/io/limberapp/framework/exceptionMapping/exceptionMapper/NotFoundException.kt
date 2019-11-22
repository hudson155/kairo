package io.limberapp.framework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.notFound.NotFoundFrameworkError
import io.limberapp.framework.exception.NotFoundException
import io.limberapp.framework.exceptionMapping.ExceptionMapper

internal class NotFoundException :
    ExceptionMapper<NotFoundException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: NotFoundException) {
        this.call.respond(HttpStatusCode.NotFound, NotFoundFrameworkError())
    }
}
