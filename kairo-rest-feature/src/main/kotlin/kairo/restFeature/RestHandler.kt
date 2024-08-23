package kairo.restFeature

import kairo.reflect.typeParam
import kotlin.reflect.KClass

/**
 * A [RestHandler] implementation is the entrypoint for a specific [RestEndpoint].
 * See this Feature's README or tests for some examples.
 */
public abstract class RestHandler<Endpoint : RestEndpoint<*, *>> {
  private val endpoint: KClass<Endpoint> = typeParam(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.parse(endpoint)

  internal fun handle() {
    // TODO.
  }
}
