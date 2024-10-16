package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kairo.rest.endpoint.RestEndpoint
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointReader] is responsible for deriving an [E] instance from a [RoutingCall].
 * Doing this dynamically at runtime involves some nontrivial JVM reflection magic,
 * but it's a huge win for reducing boilerplate.
 */
internal abstract class RestEndpointReader<out E : RestEndpoint<*, *>> {
  abstract suspend fun endpoint(call: RoutingCall): E

  internal companion object {
    fun <E : RestEndpoint<*, *>> from(endpoint: KClass<E>): RestEndpointReader<E> {
      logger.debug { "Building REST endpoint reader for endpoint $endpoint." }
      require(endpoint.isData) { "REST endpoint ${endpoint.qualifiedName!!} must be a data class or data object." }
      if (endpoint.objectInstance != null) {
        logger.debug { "Using object instance REST endpoint reader for $endpoint." }
        return DataObjectRestEndpointReader(endpoint)
      }
      logger.debug { "Using data class REST endpoint reader for $endpoint." }
      return DataClassRestEndpointReader(endpoint)
    }
  }
}
