package limber.rest.ktorPlugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException
import limber.rep.ValidationErrorsRep
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.installRoutingPlugins() {
  install(AutoHeadResponse)

  install(DoubleReceive)

  install(StatusPages) {
    exception<NotFoundException> { call, _ ->
      call.respond(HttpStatusCode.NotFound)
    }

    exception<ConstraintViolationException> { call, e ->
      call.respond(
        status = HttpStatusCode.UnprocessableEntity,
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

    exception<Throwable> { call, e ->
      logger.error(e) { "Request failed." }
      call.respond(HttpStatusCode.InternalServerError)
    }
  }
}
