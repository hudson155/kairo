package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import io.ktor.http.HttpStatusCode

abstract class ExceptionMapper<T : PiperException> {
    abstract fun handle(e: T): PiperError
}

@Suppress("FunctionName")
internal fun PiperError(
    httpStatusCode: HttpStatusCode,
    e: PiperException
) = PiperError(httpStatusCode.value, "${httpStatusCode.description}: ${e.message}")
