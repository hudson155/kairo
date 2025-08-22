package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
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
    public fun <E : RestEndpoint<*, *>> from(endpoint: KClass<E>): RestEndpointReader<E> {
      logger.debug { "Building REST endpoint reader (endpoint=$endpoint)." }
      val reader = with(RestEndpointTemplateErrorBuilder) {
        build(endpoint)
      }
      logger.debug { "Built REST endpoint reader (endpoint=$endpoint, reader=$reader)." }
      return reader
    }

    context(error: RestEndpointTemplateErrorBuilder)
    private fun <E : RestEndpoint<*, *>> build(endpoint: KClass<E>): RestEndpointReader<E> {
      require(endpoint.isData) {
        "${error.restEndpoint(endpoint)} must be a data class or data object."
      }
      if (endpoint.objectInstance != null) return DataObjectRestEndpointReader(endpoint)
      return DataClassRestEndpointReader(endpoint)
    }
  }
}
