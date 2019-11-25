package com.piperframework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import com.piperframework.error.property.missingProperty.MissingPropertyFrameworkError
import com.piperframework.exceptionMapping.ExceptionMapper

internal class InvalidTypeIdException :
    ExceptionMapper<InvalidTypeIdException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: InvalidTypeIdException) {
        this.call.respond(HttpStatusCode.BadRequest, MissingPropertyFrameworkError("type"))
    }
}
