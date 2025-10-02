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
internal class DataObjectRestEndpointReader<E : RestEndpoint<*, *>>(
  kClass: KClass<E>,
) : RestEndpointReader<E>() {
  private val objectInstance: E = checkNotNull(kClass.objectInstance)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data object." }
    return objectInstance
  }
}
