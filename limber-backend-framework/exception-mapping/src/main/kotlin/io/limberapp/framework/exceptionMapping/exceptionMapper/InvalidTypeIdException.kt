package io.limberapp.framework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.property.missingProperty.MissingPropertyFrameworkError
import io.limberapp.framework.exceptionMapping.ExceptionMapper

internal class InvalidTypeIdException :
    ExceptionMapper<InvalidTypeIdException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(
        e: InvalidTypeIdException
    ) {
        this.call.respond(
            status = HttpStatusCode.BadRequest,
            message = MissingPropertyFrameworkError("type")
        )
    }
}
