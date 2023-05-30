package limber.feature.rest

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.HttpAcceptRouteSelector
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.ParameterRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.routing
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.registerEndpoints() {
  val config = applicationContext.config

  val endpointHandlers = applicationContext.endpointHandlers

  endpointHandlers.forEach { handler ->
    val template = handler.template
    logger.info { "Registering endpoint: $template." }
    routing {
      optionallyAuthenticate(config.auth != null) {
        createRouteFromPath(template.path)
          .createChild(HttpAcceptRouteSelector(io.ktor.http.ContentType.Application.Json))
          .createChild(HttpMethodRouteSelector(template.method))
          .createChild(template.requiredQueryParams)
          .handle { handler.handle(call) }
      }
    }
  }
}

private fun Route.optionallyAuthenticate(authenticate: Boolean, build: Route.() -> Unit) {
  if (authenticate) {
    authenticate(optional = true, build = build)
  } else {
    build()
  }
}

private fun Route.createChild(requiredQueryParams: Set<String>): Route =
  requiredQueryParams.fold(this) { route, queryParam ->
    route.createChild(ParameterRouteSelector(queryParam))
  }
