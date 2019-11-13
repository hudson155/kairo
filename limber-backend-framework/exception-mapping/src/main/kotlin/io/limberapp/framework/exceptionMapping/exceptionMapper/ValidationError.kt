package io.limberapp.framework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.property.validation.ValidationFrameworkError
import io.limberapp.framework.exceptionMapping.ExceptionMapper
import io.limberapp.framework.validation.ValidationException

internal class ValidationError :
    ExceptionMapper<ValidationException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(
        e: ValidationException
    ) {
        this.call.respond(
            status = HttpStatusCode.BadRequest,
            message = ValidationFrameworkError(e.propertyName)
        )
    }
}
