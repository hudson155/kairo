package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.ConflictError
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.util.pipeline.PipelineContext

internal class ConflictException :
    ExceptionMapper<ConflictException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ConflictException) =
        Pair(HttpStatusCode.Conflict, ConflictError())
}
