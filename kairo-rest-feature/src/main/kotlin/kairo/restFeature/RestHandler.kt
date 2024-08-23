package kairo.restFeature

import io.ktor.server.routing.RoutingCall
import kairo.reflect.typeParam
import kotlin.reflect.KClass

public abstract class RestHandler<Endpoint : RestEndpoint<*, *>> {
  private val endpoint: KClass<Endpoint> = typeParam(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.parse(endpoint)

  internal fun handle(call: RoutingCall) {
    TODO("Do something with $call.")
  }
}
