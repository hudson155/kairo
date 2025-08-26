package kairo.rest

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import kairo.reflect.KairoType
import kairo.rest.reader.RestEndpointReader
import kairo.rest.template.RestEndpointTemplate
import kairo.rest.template.RestEndpointTemplateErrorBuilder
import kairo.rest.template.toKtorPath
import kotlin.reflect.KClass

private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

public class RestEndpointHandler<O : Any, E : RestEndpoint<*, O>>(
  private val endpoint: KClass<E>,
) {
  public var handle: (suspend (endpoint: E) -> O)? = null
  public var statusCode: (suspend (endpoint: O) -> HttpStatusCode?)? = null

  public fun handle(handle: suspend (endpoint: E) -> O) {
    require(this.handle == null) { "${error.endpoint(endpoint)}: Handler already defined." }
    this.handle = handle
  }

  public fun statusCode(statusCode: suspend (response: O) -> HttpStatusCode?) {
    require(this.statusCode == null) { "${error.endpoint(endpoint)}: Status code already defined." }
    this.statusCode = statusCode
  }
}

public fun <I : Any, O : Any, E : RestEndpoint<I, O>> Routing.route(
  endpoint: KClass<E>,
  block: RestEndpointHandler<O, E>.() -> Unit,
) {
  val inputType = KairoType.from<I>(RestEndpoint::class, 0, endpoint)
  val template = RestEndpointTemplate.from(endpoint)
  val route = buildRoute(template)
  val reader = RestEndpointReader.from(endpoint)
  route.handle {
    val handler = RestEndpointHandler(endpoint).apply(block)
    val response = requireNotNull(handler.handle) { "${error.endpoint(endpoint)}: Must define a handler." }
      .invoke(reader.read(call))
    handler.statusCode?.let { statusCode ->
      statusCode(response)?.let { call.response.status(it) }
    }
    call.respond(response, inputType.toKtor())
  }
}

// TODO: Authenticate.
private fun Routing.buildRoute(template: RestEndpointTemplate): Route {
  var route = createRouteFromPath(template.toKtorPath())
  route = route.createChild(HttpMethodRouteSelector(template.method))
  template.query.params.forEach { (value, required) ->
    route = if (required) route.param(value) {} else route.optionalParam(value) {}
  }
  template.contentType?.let { contentType ->
    route = route.contentType(contentType) {}
  }
  template.accept?.let { accept ->
    route = route.accept(accept) {}
  }
  return route
}
