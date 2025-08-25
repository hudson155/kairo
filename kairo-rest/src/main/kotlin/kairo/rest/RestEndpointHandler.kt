package kairo.rest

import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.optionalParam
import io.ktor.server.routing.param
import io.ktor.util.reflect.typeInfo
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kotlin.reflect.KClass
import kotlinx.serialization.serializer

public class RestEndpointHandler<I : Any, O : Any, E : RestEndpoint<I, O>>(
) {
  public var handle: (suspend (endpoint: E) -> O)? = null

  public fun handle(handle: suspend (endpoint: E) -> O) {
    this.handle = handle
  }
}

@Suppress("UseDataClass")
public class KairoRouting<E : RestEndpoint<*, *>>(
  public val input: KairoType<*>,
  public val output: KairoType<*>,
  public val endpoint: KairoType<E>,
)

public inline fun <reified I : Any, reified O : Any, reified E : RestEndpoint<I, O>> Routing.route(
  kClass: KClass<E>,
  noinline block: RestEndpointHandler<I, O, E>.() -> Unit,
) {
  with(KairoRouting(kairoType<I>(), kairoType<O>(), kairoType<E>())) {
    val template = RestEndpointTemplate.create()
    val route = buildRoute(template)
    val serializer = serializer<E>()
    val reader = RestEndpointReader.create(serializer)
    route.handle {
      val handler = RestEndpointHandler<I, O, E>().apply(block)
      val foo = reader.read(call)
      val result = handler.handle?.invoke(foo)
      call.respond(result, typeInfo<O>())
    }
  }
}

// TODO: Authenticate.
public fun Routing.buildRoute(template: RestEndpointTemplate): Route {
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
