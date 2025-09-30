package kairo.rest.reader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingCall
import kairo.reflect.KairoType
import kairo.rest.RestEndpoint
import kairo.rest.toKtor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * This is the typical case for [RestEndpointReader].
 * We use JVM reflection to inspect the [RestEndpoint] class
 * in order to determine how to get the arguments from the [RoutingCall]
 * to call the primary constructor of the data class.
 */
internal class DataClassRestEndpointReader<I : Any, E : RestEndpoint<I, *>>(
  private val json: Json,
  endpoint: KClass<E>,
) : RestEndpointReader<E>() {
  private val constructor: KFunction<E> = checkNotNull(endpoint.primaryConstructor)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data class." }
    val arguments = arguments(call)
    logger.debug { "Endpoint reader arguments (arguments=$arguments)." }
    return constructor.callBy(arguments)
  }

  private suspend fun arguments(call: ApplicationCall): Map<KParameter, Any?> =
    constructor.valueParameters.associateWith { param ->
      val paramName = checkNotNull(param.name)
      if (paramName == RestEndpoint<*, *>::body.name) {
        return@associateWith call.receive<I>(KairoType<I>(param.type).toKtor())
      }
      val serializer = json.serializersModule.serializer(param.type)
      val values = call.parameters.getAll(paramName)
      @Suppress("ElseCaseInsteadOfExhaustiveWhen")
      return@associateWith when (serializer.descriptor.kind) {
        is StructureKind.CLASS ->
          json.decodeFromJsonElement(serializer, JsonPrimitive(values?.single()))
        is SerialKind.ENUM ->
          json.decodeFromJsonElement(serializer, JsonPrimitive(values?.single()))
        is StructureKind.LIST ->
          json.decodeFromJsonElement(serializer, JsonArray(values?.map { JsonPrimitive(it) }.orEmpty()))
        is PrimitiveKind ->
          json.decodeFromJsonElement(serializer, JsonPrimitive(values?.single()))
        else -> error("Unsupported kind (kind=${serializer.descriptor.kind}).")
      }
    }
}
