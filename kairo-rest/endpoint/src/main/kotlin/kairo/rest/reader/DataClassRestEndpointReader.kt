package kairo.rest.reader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
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
internal class DataClassRestEndpointReader<I : Any, E : RestEndpoint<I, *>>(
  kClass: KClass<E>,
  json: KairoJson,
) : RestEndpointReader<E>() {
  private val json: KairoJson =
    json.copy {
      configure {
        /**
         * [ApplicationCall.parameters] always contains strings, even if the underlying type is not string-like.
         * Allowing coercion of scalars is necessary for non-string-like types to deserialize properly.
         */
        configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true)
        /**
         * Jackson has a bug where deserializing value classes can lead to trailing tokens.
         * See https://chatgpt.com/share/6965494f-a5e8-800c-ab1f-260ef33171c3.
         */
        configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false)
      }
    }

  private val constructor: KFunction<E> = checkNotNull(kClass.primaryConstructor)

  override suspend fun read(call: RoutingCall): E {
    logger.debug { "Using data class." }
    val arguments = arguments(call)
    logger.debug { "Endpoint reader arguments (arguments=$arguments)." }
    return constructor.callBy(arguments)
  }

  @OptIn(KairoJson.RawJsonMapper::class)
  private suspend fun arguments(call: ApplicationCall): Map<KParameter, Any?> =
    constructor.valueParameters.associateWith { param ->
      val paramName = checkNotNull(param.name)
      if (paramName == RestEndpoint<*, *>::body.name) {
        return@associateWith call.receive<I>(KairoType<I>(param.type).toKtor())
      }
      // val targetType = json.delegate.constructType((param.type as KClass<*>).java)
      val targetType = json.delegate.toJavaType(param.type)
      val isMultiValuedTarget = targetType.isArrayType || targetType.isCollectionLikeType
      val values = call.parameters.getAll(paramName)
      return@associateWith if (isMultiValuedTarget) {
        json.delegate.convertValue(values.orEmpty(), targetType)
      } else {
        values?.singleNullOrThrow()?.let { json.delegate.convertValue(it, targetType) }
      }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun JsonMapper.toJavaType(type: KType): JavaType {
  val kClass = type.classifier as? KClass<*>
  if (kClass != null && kClass.isValue) {
    return constructType(kClass.java)
  }
  return typeFactory.constructType(type.javaType)
}
