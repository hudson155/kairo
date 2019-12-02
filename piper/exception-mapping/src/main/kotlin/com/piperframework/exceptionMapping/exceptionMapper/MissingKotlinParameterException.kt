package com.piperframework.exceptionMapping.exceptionMapper

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.piperframework.error.error.MissingPropertyError
import com.piperframework.exceptionMapping.ExceptionMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.util.pipeline.PipelineContext

internal class MissingKotlinParameterException :
    ExceptionMapper<MissingKotlinParameterException>() {

    override suspend fun PipelineContext<Unit, ApplicationCall>.handle(e: MissingKotlinParameterException) =
        Pair(HttpStatusCode.BadRequest,
            MissingPropertyError(e.parameter.name!!)
        )
}
