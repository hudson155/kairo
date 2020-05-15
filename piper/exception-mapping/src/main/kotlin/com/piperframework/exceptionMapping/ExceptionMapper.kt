package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exception.exception.notFound.NotFoundException
import com.piperframework.util.unknownType
import io.ktor.http.HttpStatusCode

class ExceptionMapper {
  fun handle(e: PiperException): PiperError {
    val httpStatusCode = when (e) {
        is BadRequestException -> HttpStatusCode.BadRequest
        is ConflictException -> HttpStatusCode.Conflict
        is ForbiddenException -> HttpStatusCode.Forbidden
        is NotFoundException -> HttpStatusCode.NotFound
        else -> unknownType("exception", e::class)
    }
    return PiperError(httpStatusCode.value, httpStatusCode.description, e.message)
  }
}
