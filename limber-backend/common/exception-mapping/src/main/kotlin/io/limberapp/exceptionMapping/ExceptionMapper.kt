package io.limberapp.exceptionMapping

import io.ktor.http.HttpStatusCode
import io.limberapp.common.error.LimberError
import io.limberapp.common.exception.LimberException
import io.limberapp.common.exception.exception.badRequest.BadRequestException
import io.limberapp.common.exception.exception.conflict.ConflictException
import io.limberapp.common.exception.exception.forbidden.ForbiddenException
import io.limberapp.common.exception.exception.notFound.NotFoundException

object ExceptionMapper {
  fun handle(e: LimberException): LimberError {
    val httpStatusCode = when (e) {
      is BadRequestException -> HttpStatusCode.BadRequest
      is ConflictException -> HttpStatusCode.Conflict
      is ForbiddenException -> HttpStatusCode.Forbidden
      is NotFoundException -> HttpStatusCode.NotFound
      else -> unknownType("exception", e::class)
    }
    return LimberError(httpStatusCode.value, httpStatusCode.description, e.message)
  }
}
