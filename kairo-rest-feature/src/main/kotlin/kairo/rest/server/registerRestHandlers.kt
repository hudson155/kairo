package kairo.rest.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.application.pluginOrNull
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.application
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import io.ktor.server.routing.routing
import kairo.rest.handler.RestHandler
import kairo.rest.printer.KtorPathTemplateRestEndpointPrinter
import kairo.rest.template.RestEndpointTemplate

private val logger: KLogger = KotlinLogging.logger {}

public fun Application.registerRestHandlers(handlers: Set<RestHandler<*, *>>) {
  logger.info { "Registering ${handlers.size} REST handlers." }
  handlers.forEach { handler ->
    val template = handler.template
    logger.info { "Registering REST handler: $template." }
    routing {
      kairoAuthenticate {
        route(template).handle {
          handler.handle(call)
        }
      }
    }
  }
}

public fun Route.kairoAuthenticate(build: Route.() -> Unit) {
  val authenticationIsInstalled = application.pluginOrNull(Authentication) != null
  if (authenticationIsInstalled) {
    authenticate(optional = true) {
      build()
    }
  } else {
    build()
  }
}

public fun Route.route(template: RestEndpointTemplate): Route {
  var route = createRouteFromPath(KtorPathTemplateRestEndpointPrinter.write(template))
  route = route.createChild(HttpMethodRouteSelector(template.method))
  template.query.params.forEach { (value, required) ->
    route = if (required) route.param(value) {} else route.optionalParam(value) {}
  }
  if (template.contentType != null) {
    route = route.contentType(template.contentType) {}
  }
  if (template.accept != null) {
    route = route.accept(template.accept) {}
  }
  return route
}
