package com.piperframework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.piperframework.error.property.missingProperty.MissingPropertyFrameworkError
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

internal class MissingKotlinParameterException :
    ExceptionMapper<MissingKotlinParameterException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: MissingKotlinParameterException) {
        this.call.respond(HttpStatusCode.BadRequest, MissingPropertyFrameworkError(e.parameter.name!!))
    }
}
