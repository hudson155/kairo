package kairo.rest.server

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import kairo.rest.exception.EndpointNotFound
import kairo.rest.exceptionHandler.ExceptionManager

internal fun Application.installStatusPages() {
  val exceptionManager = ExceptionManager()
  install(StatusPages) {
    exception<Throwable>(exceptionManager::handle)
    status(HttpStatusCode.NotFound) { call, _ ->
      exceptionManager.handle(call, EndpointNotFound())
    }
  }
}
