package kairo.rest.handler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.routing.RoutingCall

private val logger: KLogger = KotlinLogging.logger {}

internal fun RoutingCall.log() {
  val statusCode = response.status()
  val method = request.httpMethod
  val path = request.path()
  logger.info { "${statusCode ?: "Unhandled"}: ${method.value} $path" }
}
