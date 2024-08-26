package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointReader] is responsible for deriving an [Endpoint] instance from a [RoutingCall].
 * Doing this dynamically at runtime involves some nontrivial JVM reflection magic,
 * but it's a huge win for reducing boilerplate.
 */
internal abstract class RestEndpointReader<out Endpoint : RestEndpoint<*, *>> {
  abstract suspend fun endpoint(call: RoutingCall): Endpoint

  internal companion object {
    fun <Endpoint : RestEndpoint<*, *>> from(endpoint: KClass<Endpoint>): RestEndpointReader<Endpoint> {
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
