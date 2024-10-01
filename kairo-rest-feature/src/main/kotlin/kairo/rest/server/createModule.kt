package kairo.rest.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import io.ktor.server.routing.routing
import kairo.rest.auth.AuthVerifier
import kairo.rest.handler.RestHandler
import kairo.rest.printer.KtorPathTemplateRestEndpointPrinter
import kairo.rest.template.RestEndpointTemplate

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [createModule] is not a great name for a function, so here's a better (but brief) explanation.
 * Ktor servers have "modules", which are basically just extension functions on [Application].
 * Kairo does not use Ktor's module system.
 * [createModule] returns a function that sets up a single module.
 */
internal fun createModule(
  authVerifiers: List<AuthVerifier<*>>,
  handlers: Set<RestHandler<*, *>>,
): Application.() -> Unit =
  {
    installAuth(authVerifiers)
    installContentNegotiation()
    installStatusPages()
    registerRestHandlers(handlers)
  }

private fun Application.registerRestHandlers(handlers: Set<RestHandler<*, *>>) {
  logger.info { "Registering ${handlers.size} REST handlers." }
  handlers.forEach { handler ->
    val template = handler.template
    logger.info { "Registering REST handler: $template." }
    routing {
      authenticate(optional = true) {
        route(template).handle { handler.handle(call) }
      }
    }
  }
}

internal fun Route.route(template: RestEndpointTemplate): Route {
  var route = createRouteFromPath(KtorPathTemplateRestEndpointPrinter.write(template))
  route = route.createChild(HttpMethodRouteSelector(template.method))
  template.query.params.forEach { param ->
    val value = param.value
    route = if (param.required) route.param(value) {} else route.optionalParam(value) {}
  }
  if (template.contentType != null) {
    route = route.contentType(template.contentType) {}
  }
  if (template.accept != null) {
    route = route.accept(template.accept) {}
  }
  return route
}
