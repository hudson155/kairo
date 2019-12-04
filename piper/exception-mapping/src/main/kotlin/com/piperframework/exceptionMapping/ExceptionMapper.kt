package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

internal abstract class ExceptionMapper<T : PiperException> {

    val handler: suspend PipelineContext<Unit, ApplicationCall>.(T) -> Unit =
        { e -> with(handle(e)) { call.respond(HttpStatusCode.fromValue(statusCode), this) } }

    abstract suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: T): PiperError
}
