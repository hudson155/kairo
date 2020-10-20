package io.limberapp.common.exception

import io.ktor.http.HttpStatusCode
import io.limberapp.common.exception.badRequest.BadRequestException
import io.limberapp.common.exception.conflict.ConflictException
import io.limberapp.common.exception.forbidden.ForbiddenException
import io.limberapp.common.exception.notFound.NotFoundException
import io.limberapp.common.exception.unprocessableEntity.UnprocessableEntityException
import io.limberapp.common.rep.LimberError

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
