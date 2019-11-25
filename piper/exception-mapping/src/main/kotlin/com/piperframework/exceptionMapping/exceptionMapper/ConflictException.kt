package com.piperframework.exceptionMapping.exceptionMapper

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import com.piperframework.error.conflict.ConflictFrameworkError
import com.piperframework.exception.ConflictException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class ConflictException :
    ExceptionMapper<ConflictException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: ConflictException) {
        this.call.respond(HttpStatusCode.Conflict, ConflictFrameworkError())
    }
}
