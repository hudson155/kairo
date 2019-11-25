package com.piperframework.exceptionMapping

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

internal abstract class ExceptionMapper<T : Throwable> {

    val handler: suspend PipelineContext<Unit, ApplicationCall>.(T) -> Unit = { e -> handle(e) }

    abstract suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: T)
}
