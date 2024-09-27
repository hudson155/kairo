package kairo.rest.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingFailureStatusCode
import io.ktor.utils.io.InternalAPI
import kairo.rest.exception.AcceptMismatch
import kairo.rest.exception.ContentTypeMismatch
import kairo.rest.exception.EndpointNotFound
import kairo.rest.exceptionHandler.ExceptionManager

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.installStatusPages() {
  val exceptionManager = ExceptionManager()

  install(StatusPages) {
    exception<Throwable>(exceptionManager::handle)

    @OptIn(InternalAPI::class)
    unhandled { call ->
      call.attributes.getOrNull<HttpStatusCode>(RoutingFailureStatusCode)?.let<HttpStatusCode, Unit> { statusCode ->
        when (statusCode) {
          in setOf(HttpStatusCode.BadRequest, HttpStatusCode.NotFound, HttpStatusCode.MethodNotAllowed) -> {
            exceptionManager.handle(call, EndpointNotFound())
            return@unhandled
          }
          HttpStatusCode.NotAcceptable -> {
            exceptionManager.handle(call, AcceptMismatch())
            return@unhandled
          }
          HttpStatusCode.UnsupportedMediaType -> {
            exceptionManager.handle(call, ContentTypeMismatch())
            return@unhandled
          }
        }
      }

      // If the exception wasn't handled by any of the exception handlers...
      logger.error { "Unhandled request." }
      call.respond<HttpStatusCode>(HttpStatusCode.InternalServerError)
    }
  }
}
