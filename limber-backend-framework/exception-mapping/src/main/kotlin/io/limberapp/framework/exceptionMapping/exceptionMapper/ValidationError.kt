package io.limberapp.framework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.property.validation.ValidationFrameworkError
import io.limberapp.framework.exceptionMapping.ExceptionMapper
import io.limberapp.framework.validation.validation.ValidationError

internal class ValidationError :
    ExceptionMapper<ValidationError>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(
        e: ValidationError
    ) {
        this.call.respond(
            status = HttpStatusCode.BadRequest,
            message = ValidationFrameworkError(e.propertyName)
        )
    }
}
