package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Some [RestEndpoint]s are singleton objects (they will be data objects).
 * This makes our job easy, since we just return the singleton!
 */
public class DataObjectRestEndpointReader<I : Any, out E : RestEndpoint<I, *>>(
  endpoint: KClass<E>,
) : RestEndpointReader<I, E>() {
  private val objectInstance: E = checkNotNull(endpoint.objectInstance)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data object." }
    return objectInstance
  }
}
