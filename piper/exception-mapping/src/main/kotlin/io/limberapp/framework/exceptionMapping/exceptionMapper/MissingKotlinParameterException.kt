package io.limberapp.framework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.property.missingProperty.MissingPropertyFrameworkError
import io.limberapp.framework.exceptionMapping.ExceptionMapper

internal class MissingKotlinParameterException :
    ExceptionMapper<MissingKotlinParameterException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: MissingKotlinParameterException) {
        this.call.respond(HttpStatusCode.BadRequest, MissingPropertyFrameworkError(e.parameter.name!!))
    }
}
