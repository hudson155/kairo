package kairo.rest.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.routing.RoutingFailureStatusCode
import io.ktor.utils.io.InternalAPI
import kairo.rest.exception.AcceptMismatch
import kairo.rest.exception.ContentTypeMismatch
import kairo.rest.exception.EndpointNotFound
import kairo.rest.exceptionHandler.ExceptionManager
import kairo.rest.exceptionHandler.respondWithError

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.installStatusPages(exceptionManager: ExceptionManager) {
  install(StatusPages) {
    exception<Exception> { call, cause ->
      call.respondWithError(exceptionManager.handle(cause))
    }

    @OptIn(InternalAPI::class)
    unhandled { call ->
      call.attributes.getOrNull(RoutingFailureStatusCode)?.let { statusCode ->
        when (statusCode) {
          HttpStatusCode.BadRequest, HttpStatusCode.NotFound, HttpStatusCode.MethodNotAllowed -> {
            call.respondWithError(exceptionManager.handle(EndpointNotFound()))
            return@unhandled
          }
          HttpStatusCode.NotAcceptable -> {
            call.respondWithError(exceptionManager.handle(AcceptMismatch()))
            return@unhandled
          }
          HttpStatusCode.UnsupportedMediaType -> {
            call.respondWithError(exceptionManager.handle(ContentTypeMismatch()))
            return@unhandled
          }
        }
      }

      // If the exception wasn't handled by any of the exception handlers...
      logger.error { "Unhandled request." }
      call.respondWithError(Pair(HttpStatusCode.InternalServerError, null))
    }
  }
}
