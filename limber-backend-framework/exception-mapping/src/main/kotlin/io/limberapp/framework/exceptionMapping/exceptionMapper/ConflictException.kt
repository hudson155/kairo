package io.limberapp.framework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.conflict.ConflictFrameworkError
import io.limberapp.framework.error.notFound.NotFoundFrameworkError
import io.limberapp.framework.exception.ConflictException
import io.limberapp.framework.exceptionMapping.ExceptionMapper

internal class ConflictException :
    ExceptionMapper<ConflictException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(
        e: ConflictException
    ) {
        this.call.respond(
            status = HttpStatusCode.Conflict,
            message = ConflictFrameworkError()
        )
    }
}
