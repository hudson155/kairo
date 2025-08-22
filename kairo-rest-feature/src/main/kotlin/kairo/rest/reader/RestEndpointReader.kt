package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import java.util.concurrent.ConcurrentHashMap
import kairo.rest.endpoint.RestEndpoint
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointReader] is responsible for deriving an [E] instance from a [RoutingCall].
 * Doing this dynamically at runtime involves some nontrivial JVM reflection magic,
 * but it's a huge win for reducing boilerplate.
 */
public abstract class RestEndpointReader<out E : RestEndpoint<*, *>> {
  public abstract suspend fun endpoint(call: RoutingCall): E

  public companion object {
    private val cache: MutableMap<KClass<out RestEndpoint<*, *>>, RestEndpointReader<*>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    public fun <E : RestEndpoint<*, *>> from(endpointKClass: KClass<E>): RestEndpointReader<E> =
      cache.computeIfAbsent(endpointKClass) {
        build(endpointKClass)
      } as RestEndpointReader<E>

    private fun <E : RestEndpoint<*, *>> build(endpointKClass: KClass<E>): RestEndpointReader<E> {
      logger.debug { "Building REST endpoint reader for endpoint $endpointKClass." }
      require(endpointKClass.isData) {
        "REST endpoint ${endpointKClass.qualifiedName!!} must be a data class or data object."
      }
      if (endpointKClass.objectInstance != null) {
        logger.debug { "Using object instance REST endpoint reader for $endpointKClass." }
        return DataObjectRestEndpointReader(endpointKClass)
      }
      logger.debug { "Using data class REST endpoint reader for $endpointKClass." }
      return DataClassRestEndpointReader(endpointKClass)
    }
  }
}
