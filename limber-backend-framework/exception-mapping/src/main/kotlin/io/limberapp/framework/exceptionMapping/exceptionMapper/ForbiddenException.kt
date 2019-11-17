package io.limberapp.framework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.forbidden.ForbiddenFrameworkError
import io.limberapp.framework.exception.ForbiddenException
import io.limberapp.framework.exceptionMapping.ExceptionMapper

internal class ForbiddenException :
    ExceptionMapper<ForbiddenException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(
        e: ForbiddenException
    ) {
        this.call.respond(
            status = HttpStatusCode.Forbidden,
            message = ForbiddenFrameworkError()
        )
    }
}
