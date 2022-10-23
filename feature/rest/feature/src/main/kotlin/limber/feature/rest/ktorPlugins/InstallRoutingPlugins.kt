package limber.feature.rest.ktorPlugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.installRoutingPlugins() {
  install(AutoHeadResponse)

  install(DoubleReceive)

  install(StatusPages) {
    exception<Throwable> { call, e ->
      logger.error(e) { "Request failed." }
      call.respond(HttpStatusCode.InternalServerError)
    }
  }
}
