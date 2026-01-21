package kairo.rest.reader

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.MapperFeature
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingCall
import kairo.reflect.KairoType
import kairo.rest.RestEndpoint
import kairo.rest.toKtor
import kairo.serialization.KairoJson
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.javaType

private val logger: KLogger = KotlinLogging.logger {}

/**
 * This is the typical case for [RestEndpointReader].
 * We use JVM reflection to inspect the [RestEndpoint] class
 * in order to determine how to get the arguments from the [RoutingCall]
 * to call the primary constructor of the data class.
 */
@OptIn(KairoJson.RawJsonMapper::class)
internal class DataClassRestEndpointReader<I : Any, E : RestEndpoint<I, *>>(
  kClass: KClass<E>,
  json: KairoJson,
) : RestEndpointReader<E>() {
  private val json: KairoJson =
    json.copy {
      /**
       * [ApplicationCall.parameters] always contains strings, even if the underlying type is not string-like.
       * Allowing coercion of scalars is necessary for non-string-like types to deserialize properly.
       */
      configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true)
    }

  private val constructor: KFunction<E> = checkNotNull(kClass.primaryConstructor)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data class." }
    val arguments = arguments(call)
    logger.debug { "Endpoint reader arguments (arguments=$arguments)." }
    return constructor.callBy(arguments)
  }

  private suspend fun arguments(call: ApplicationCall): Map<KParameter, Any?> =
    buildMap {
      constructor.valueParameters.forEach { param ->
        val paramName = checkNotNull(param.name)
        if (paramName == RestEndpoint<*, *>::body.name) {
          put(param, call.receive<I>(KairoType<I>(param.type).toKtor()))
          return@forEach
        }
        val paramType = javaType(param.type)
        val values = call.parameters.getAll(paramName)
        if (paramType.isArrayType || paramType.isCollectionLikeType) {
          put(param, json.delegate.convertValue(values.orEmpty(), paramType))
        } else if (values != null) {
          put(param, json.delegate.convertValue(values.singleNullOrThrow(), paramType))
        }
      }
    }

  /**
   * Makes a best-effort attempt to extract the [JavaType] from the [KType].
   * This is not tested comprehensively and might need to be updated in the future.
   */
  @OptIn(ExperimentalStdlibApi::class)
  private fun javaType(type: KType): JavaType {
    val kClass = type.classifier as? KClass<*>
    if (kClass != null && kClass.isValue) {
      /**
       * If it's a value class, we can't use [KType.javaType] since it will give the underlying type.
       * However, we can't use this approach regularly because it erases generic type information.
       */
      return json.delegate.constructType(kClass.java)
    }
    return json.delegate.constructType(type.javaType)
  }
}
