package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Routing
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import io.ktor.server.routing.routing

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [createModule] is not a great name for a function, so here's a better (but brief) explanation.
 * Ktor servers have "modules", which are basically just extension functions on [Application].
 * Kairo does not use Ktor's module system.
 * [createModule] returns a function that sets up a single module.
 */
internal fun createModule(handlers: Set<RestHandler<*>>): Application.() -> Unit {
  return {
    logger.info { "Registering ${handlers.size} REST handlers." }
    handlers.forEach { handler ->
      val template = handler.template
      logger.info { "Registering REST handler: $template." }
      routing {
        route(template).handle { handler.handle(call) }
      }
    }
  }
}

internal fun Routing.route(template: RestEndpointTemplate): Routing {
  var route = createRouteFromPath(KtorPathTemplateRestEndpointWriter.write(template))
  route = route.createChild(HttpMethodRouteSelector(template.method))
  template.query.params.forEach { param ->
    val value = param.value
    route = if (param.required) route.param(value) {} else route.optionalParam(value) {}
  }
  template.contentType?.let { route = route.contentType(template.contentType) {} }
  route = route.accept(template.accept) {}
  return route
}

public sealed class HttpResult {
  public data class Result<T : Any>(val value: T, val httpStatus: HttpStatusCode) : HttpResult()

  public data object NotFound

  public data object Unhandled
}
