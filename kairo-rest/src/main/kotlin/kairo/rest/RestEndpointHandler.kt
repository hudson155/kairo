package kairo.rest

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.application
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import kairo.reflect.KairoType
import kairo.rest.auth.AuthReceiver
import kairo.rest.auth.authConfig
import kairo.rest.reader.RestEndpointReader
import kairo.rest.template.RestEndpointTemplate
import kairo.rest.template.toKtorPath
import kotlin.reflect.KClass

private val error: RestEndpointErrorBuilder = RestEndpointErrorBuilder

/**
 * REST endpoint handlers are invoked for each call to the matching endpoint.
 */
public class RestEndpointHandler<O : Any, E : RestEndpoint<*, O>> internal constructor(
  private val kClass: KClass<E>,
) {
  internal val auth: MutableList<(suspend AuthReceiver<E>.() -> Unit)> = mutableListOf()
  internal var handle: (suspend HandleReceiver<E>.() -> O)? = null
  internal var statusCode: (suspend StatusCodeReceiver<O>.() -> HttpStatusCode?)? = null

  /**
   * Specifies auth for the endpoint.
   */
  public fun auth(auth: suspend AuthReceiver<E>.() -> Unit) {
    require(this.auth.isEmpty()) { "${error.endpoint(kClass)}: Auth already defined." }
    this.auth += auth
  }

  /**
   * Specifies overriding auth for the endpoint.
   */
  public fun authOverriddenBy(auth: suspend AuthReceiver<E>.() -> Unit) {
    require(this.auth.isNotEmpty()) { "${error.endpoint(kClass)}: Auth not defined." }
    this.auth += auth
  }

  /**
   * Specifies the handler for the endpoint.
   */
  public fun handle(handle: suspend HandleReceiver<E>.() -> O) {
    require(this.handle == null) { "${error.endpoint(kClass)}: Handler already defined." }
    this.handle = handle
  }

  /**
   * Specifies the HTTP status code to use for the response.
   * If this is not provided or returns null, Ktor's default will apply.
   */
  public fun statusCode(statusCode: suspend StatusCodeReceiver<O>.() -> HttpStatusCode?) {
    require(this.statusCode == null) { "${error.endpoint(kClass)}: Status code already defined." }
    this.statusCode = statusCode
  }
}

/**
 * Routes a [RestEndpoint] with Ktor.
 * The [block] must specify a handler for the endpoint ([RestEndpointHandler.handle]).
 */
public fun <I : Any, O : Any, E : RestEndpoint<I, O>> Route.route(
  kClass: KClass<E>,
  block: RestEndpointHandler<O, E>.() -> Unit,
) {
  val responseType = KairoType.from<O>(RestEndpoint::class, 1, kClass)
  val template = RestEndpointTemplate.from(kClass)
  val route = buildRoute(template)
  val reader = RestEndpointReader.from(kClass, application.json)
  route.handle {
    val handler = RestEndpointHandler(kClass).apply(block)
    val endpoint = reader.read(call)
    application.authConfig?.let { authConfig ->
      AuthReceiver(
        call = call,
        endpoint = endpoint,
        default = with(authConfig) { { default() } },
      ).apply {
        handler.auth.firstOrNull()?.let { auth(it) }
        handler.auth.drop(1).forEach { authOverriddenBy(it) }
        authOverriddenBy(with(authConfig) { { fallback() } })
      }.getOrThrow()
    }
    val response = requireNotNull(handler.handle) { "${error.endpoint(kClass)}: Must define a handler." }
      .invoke(HandleReceiver(call, endpoint))
    handler.statusCode?.let { statusCode ->
      StatusCodeReceiver(call, response).statusCode()?.let { call.response.status(it) }
    }
    call.respond(response, responseType.toKtor())
  }
}

private fun Route.buildRoute(template: RestEndpointTemplate): Route {
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
