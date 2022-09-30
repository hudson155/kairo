package limber.rest.wrapper

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException
import limber.rep.ValidationErrorsRep
import limber.rest.exception.UnprocessableException
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal suspend fun withErrorHandling(call: ApplicationCall, block: suspend () -> Unit) {
  try {
    block()
  } catch (e: ConstraintViolationException) {
    call.handleConstraintViolation(e)
  } catch (e: NotFoundException) {
    call.handleNotFound(e)
  } catch (e: UnprocessableException) {
    call.handleUnprocessable(e)
  }
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
  respond(HttpStatusCode.NotFound)
}

private suspend fun ApplicationCall.handleUnprocessable(e: UnprocessableException) {
  logger.debug(e) { "Unprocessable." }
  respond(HttpStatusCode.UnprocessableEntity)
}
