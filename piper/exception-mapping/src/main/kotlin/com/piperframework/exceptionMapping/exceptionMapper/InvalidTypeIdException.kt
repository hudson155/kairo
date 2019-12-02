package com.piperframework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.piperframework.error.error.MissingPropertyError
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.util.pipeline.PipelineContext

internal class InvalidTypeIdException :
    ExceptionMapper<InvalidTypeIdException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: InvalidTypeIdException) =
        Pair(HttpStatusCode.BadRequest, MissingPropertyError("type"))
}
