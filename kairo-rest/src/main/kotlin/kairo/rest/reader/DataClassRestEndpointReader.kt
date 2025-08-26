package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingCall
import io.ktor.util.reflect.TypeInfo
import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * This is the typical case for [RestEndpointReader].
 * We use JVM reflection to inspect the [RestEndpoint] class
 * in order to determine how to get the arguments from the [RoutingCall]
 * to call the primary constructor of the data class.
 */
public class DataClassRestEndpointReader<I : Any, out E : RestEndpoint<I, *>>(
  endpoint: KClass<E>,
) : RestEndpointReader<I, E>() {
  private val constructor: KFunction<E> = checkNotNull(endpoint.primaryConstructor)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data class." }
    val arguments = arguments(call)
    logger.debug { "Endpoint reader arguments (arguments=$arguments)." }
    return constructor.callBy(arguments)
  }

  @Suppress("UNCHECKED_CAST")
  private fun arguments(call: ApplicationCall): Map<KParameter, Any?> =
    constructor.valueParameters.associateWith { param ->
      if (param.name == RestEndpoint<*, *>::body.name) {
        return@associateWith suspend { call.receive<I>(TypeInfo(param.type.classifier as KClass<I>, param.type)) }
      }
      val thisSerializer = Json.serializersModule.serializer(param.type)
      val values = call.parameters.getAll(param.name!!)!!
      return@associateWith when (thisSerializer.descriptor.kind) {
        is PrimitiveKind -> Json.decodeFromJsonElement(thisSerializer, JsonPrimitive(values.single()))
        else -> error("Unsupported kind: ${thisSerializer.descriptor.kind}.")
      }
    }
}
