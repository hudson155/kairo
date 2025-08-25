package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kotlinx.serialization.KSerializer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointReader] is responsible for deriving an [E] instance from a [RoutingCall].
 * Doing this dynamically at runtime involves some nontrivial JVM reflection magic,
 * but it's a huge win for reducing boilerplate.
 */
public abstract class RestEndpointReader<I : Any, out E : RestEndpoint<I, *>> {
  public abstract suspend fun read(call: RoutingCall): E

  public companion object {
    context(routing: KairoRouting<E>)
    public fun <I : Any, E : RestEndpoint<I, *>> create(
      serializer: KSerializer<E>,
    ): RestEndpointReader<I, E> {
      logger.debug { "Building REST endpoint reader (endpoint=${routing.endpoint.kotlinClass})." }
      val reader = with(RestEndpointTemplateErrorBuilder) {
        build(serializer)
      }
      logger.debug { "Built REST endpoint reader (endpoint=${routing.endpoint.kotlinClass}, reader=$reader)." }
      return reader
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<E>)
    private fun <I : Any, E : RestEndpoint<I, *>> build(
      serializer: KSerializer<E>,
    ): RestEndpointReader<I, E> {
      val endpoint = routing.endpoint.kotlinClass
      require(endpoint.isData) {
        "${error.endpoint()} must be a data class or data object."
      }
      if (endpoint.objectInstance != null) return DataObjectRestEndpointReader(endpoint)
      return DataClassRestEndpointReader(endpoint, serializer)
    }
  }
}
