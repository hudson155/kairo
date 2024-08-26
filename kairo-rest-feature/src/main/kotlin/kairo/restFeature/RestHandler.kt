package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kairo.reflect.typeParam
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A [RestHandler] implementation is the entrypoint for a specific [RestEndpoint].
 * See this Feature's README or tests for some examples.
 */
public abstract class RestHandler<Endpoint : RestEndpoint<*, *>> {
  private val endpoint: KClass<Endpoint> = typeParam(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.from(endpoint)
  private val reader: RestEndpointReader<Endpoint> = RestEndpointReader.from(endpoint)

  internal suspend fun handle(call: RoutingCall) {
    val endpoint = reader.endpoint(call)
    logger.info { "Got endpoint: $endpoint." } // TODO: Switch this to debug.
  }
}
