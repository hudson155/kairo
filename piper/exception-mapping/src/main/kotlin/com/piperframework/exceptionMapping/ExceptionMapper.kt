package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exception.exception.notFound.NotFoundException
import io.ktor.http.HttpStatusCode

class ExceptionMapper {

    fun handle(e: PiperException): PiperError {
        return when (e) {
            is BadRequestException -> PiperError(HttpStatusCode.BadRequest, e)
            is ConflictException -> PiperError(HttpStatusCode.Conflict, e)
            is ForbiddenException -> PiperError(HttpStatusCode.Forbidden, e)
            is NotFoundException -> PiperError(HttpStatusCode.NotFound, e)
            else -> error("Unknown exception type: ${e::class.simpleName}")
        }
    }
}

@Suppress("FunctionName")
private fun PiperError(
    httpStatusCode: HttpStatusCode,
    e: PiperException
) = PiperError(httpStatusCode.value, "${httpStatusCode.description}: ${e.message}")
