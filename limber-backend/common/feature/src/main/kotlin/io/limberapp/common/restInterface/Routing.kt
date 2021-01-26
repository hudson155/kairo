package io.limberapp.common.restInterface

import io.ktor.routing.HttpAcceptRouteSelector
import io.ktor.routing.HttpMethodRouteSelector
import io.ktor.routing.ParameterRouteSelector
import io.ktor.routing.Route
import io.ktor.routing.createRouteFromPath
import io.ktor.util.pipeline.ContextDsl

/**
 * Builds a Ktor route to match specified [endpointTemplate]. This should be used to easily wire up
 * endpoints to a Ktor application.
 */
@ContextDsl
fun Route.route(
    endpointTemplate: EndpointTemplate<*>,
    build: Route.() -> Unit,
): Route {
  val pathRoute = createRouteFromPath(endpointTemplate.pathTemplate)
      .createChild(HttpAcceptRouteSelector(endpointTemplate.contentType))
      .createChild(HttpMethodRouteSelector(endpointTemplate.httpMethod))
  val fullRoute = endpointTemplate.requiredQueryParams.fold(pathRoute) { route, queryParam ->
    route.createChild(ParameterRouteSelector(queryParam))
  }
  return fullRoute.apply(build)
}
