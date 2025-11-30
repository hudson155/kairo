package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplateErrorBuilder
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointReader] is responsible for deriving an [E] instance from a [RoutingCall].
 * Doing this dynamically at runtime involves some nontrivial JVM reflection magic,
 * but it's a huge win for reducing boilerplate.
 */
internal abstract class RestEndpointReader<E : RestEndpoint<*, *>> {
  abstract suspend fun read(call: RoutingCall): E

  internal companion object {
    private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

    fun <I : Any, E : RestEndpoint<I, *>> from(kClass: KClass<E>, json: Json = Json): RestEndpointReader<E> {
      logger.debug { "Building REST endpoint reader (endpoint=$kClass)." }
      require(kClass.isData) { "${error.endpoint(kClass)}: Must be a data class or data object." }
      val reader =
        if (kClass.objectInstance != null) {
          DataObjectRestEndpointReader(kClass)
        } else {
          DataClassRestEndpointReader(kClass, json)
        }
      logger.debug { "Built REST endpoint reader (endpoint=$kClass, reader=$reader)." }
      return reader
    }
  }
}
