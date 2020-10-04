package io.limberapp.exceptionMapping

import io.ktor.http.HttpStatusCode
import io.limberapp.error.LimberError
import io.limberapp.exception.LimberException
import io.limberapp.exception.badRequest.BadRequestException
import io.limberapp.exception.conflict.ConflictException
import io.limberapp.exception.forbidden.ForbiddenException
import io.limberapp.exception.notFound.NotFoundException
import io.limberapp.exception.unprocessableEntity.UnprocessableEntityException

object ExceptionMapper {
  fun handle(e: LimberException): LimberError {
    val httpStatusCode = when (e) {
      is BadRequestException -> HttpStatusCode.BadRequest
      is ConflictException -> HttpStatusCode.Conflict
      is ForbiddenException -> HttpStatusCode.Forbidden
      is NotFoundException -> HttpStatusCode.NotFound
      is UnprocessableEntityException -> HttpStatusCode.UnprocessableEntity
      else -> unknownType("exception", e::class)
    }
    return LimberError(httpStatusCode.value, httpStatusCode.description, e.message)
  }
}
