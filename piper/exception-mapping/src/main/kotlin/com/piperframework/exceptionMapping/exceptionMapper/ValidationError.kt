package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ValidationError
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.validation.ValidationException
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.util.pipeline.PipelineContext

internal class ValidationError :
    ExceptionMapper<ValidationException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ValidationException) =
        Pair(HttpStatusCode.BadRequest, ValidationError(e.propertyName))
}
