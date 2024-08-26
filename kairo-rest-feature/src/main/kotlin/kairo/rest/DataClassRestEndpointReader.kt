package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.RoutingCall
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

private val logger: KLogger = KotlinLogging.logger {}

/**
 * This is the typical case for [RestEndpointReader].
 * We use JVM reflection to inspect the [RestEndpoint] class
 * in order to determine how to get the arguments from the [RoutingCall]
 * to call the primary constructor of the data class.
 */
internal class DataClassRestEndpointReader<out Endpoint : RestEndpoint<*, *>>(
  endpoint: KClass<Endpoint>,
) : RestEndpointReader<Endpoint>() {
  private val constructor: KFunction<Endpoint> =
    checkNotNull(endpoint.primaryConstructor)

  override suspend fun endpoint(call: RoutingCall): Endpoint {
    logger.debug { "Creating data class." }
    val arguments = arguments(call)
    logger.debug { "Arguments: $arguments." }
    return constructor.callBy(arguments.mapValues { (_, argument) -> argument.read() })
  }

  private fun arguments(call: ApplicationCall): Map<KParameter, RestEndpointArgument> {
    return constructor.valueParameters.associateWith { param ->
      if (param.name == RestEndpoint<*, *>::body.name) {
        return@associateWith RestEndpointArgument.Body(call, param)
      }
      return@associateWith RestEndpointArgument.Param(call, param)
    }
  }
}
