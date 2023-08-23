package limber.feature.rest.wrapper

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException
import limber.exception.AuthException
import limber.exception.ConflictException
import limber.exception.UnprocessableException
import limber.rep.ConflictErrorRep
import limber.rep.ForbiddenErrorRep
import limber.rep.NotFoundErrorRep
import limber.rep.UnauthorizedErrorRep
import limber.rep.UnprocessableErrorRep
import limber.rep.ValidationErrorsRep
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal suspend fun withErrorHandling(call: ApplicationCall, block: suspend () -> Unit) {
  try {
    block()
  } catch (e: AuthException) {
    call.handleAuth(e)
  } catch (e: ConflictException) {
    call.handleConflict(e)
  } catch (e: ConstraintViolationException) {
    call.handleConstraintViolation(e)
  } catch (e: NotFoundException) {
    call.handleNotFound(e)
  } catch (e: UnprocessableException) {
    call.handleUnprocessable(e)
  }
}

private suspend fun ApplicationCall.handleAuth(e: AuthException) {
  logger.debug(e) { "${e.status}." }
  when (e.status) {
    AuthException.Status.Unauthorized -> {
      respond(
        status = HttpStatusCode.Unauthorized,
        message = UnauthorizedErrorRep(e.userMessage),
      )
    }
    AuthException.Status.Forbidden -> {
      respond(
        status = HttpStatusCode.Forbidden,
        message = ForbiddenErrorRep(e.userMessage),
      )
    }
  }
}

private suspend fun ApplicationCall.handleConflict(e: ConflictException) {
  logger.debug(e) { "Conflict." }
  respond(
    status = HttpStatusCode.Conflict,
    message = ConflictErrorRep(e.message),
  )
}

private suspend fun ApplicationCall.handleConstraintViolation(e: ConstraintViolationException) {
  logger.debug(e) { "Constraint violation." }
  respond(
    status = HttpStatusCode.BadRequest,
    message = ValidationErrorsRep(
      errors = e.constraintViolations.map {
        ValidationErrorsRep.ValidationError(
          propertyPath = it.propertyPath.toString(),
          message = it.message,
        )
      },
    ),
  )
}

private suspend fun ApplicationCall.handleNotFound(e: NotFoundException) {
  logger.debug(e) { "Not found." }
  respond(
    status = HttpStatusCode.NotFound,
    message = NotFoundErrorRep,
  )
}

private suspend fun ApplicationCall.handleUnprocessable(e: UnprocessableException) {
  logger.debug(e) { "Unprocessable." }
  respond(
    status = HttpStatusCode.UnprocessableEntity,
    message = UnprocessableErrorRep(e.message),
  )
}
