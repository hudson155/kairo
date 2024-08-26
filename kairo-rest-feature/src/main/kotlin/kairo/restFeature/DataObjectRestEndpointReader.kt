package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Some [RestEndpoint]s are singleton objects (they will be data objects).
 * This makes our job easy, since we just return the singleton!
 */
internal class DataObjectRestEndpointReader<out Endpoint : RestEndpoint<*, *>>(
  endpoint: KClass<Endpoint>,
) : RestEndpointReader<Endpoint>() {
  private val objectInstance: Endpoint =
    checkNotNull(endpoint.objectInstance)

  override suspend fun endpoint(call: RoutingCall): Endpoint {
    logger.debug { "Using object instance." }
    return objectInstance
  }
}
