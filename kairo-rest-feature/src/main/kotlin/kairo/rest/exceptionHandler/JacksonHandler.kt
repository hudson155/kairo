@file:Suppress("DEPRECATION", "ForbiddenImport")

package kairo.rest.exceptionHandler

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import kairo.rest.exception.InvalidProperty
import kairo.rest.exception.JsonBadRequestException
import kairo.rest.exception.MalformedJson
import kairo.rest.exception.MissingRequiredProperty
import kairo.rest.exception.UnknownJsonError
import kairo.rest.exception.UnrecognizedPolymorphicType
import kairo.rest.exception.UnrecognizedProperty
import kairo.rest.reader.RestEndpointParamException
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Converts Jackson deserialization exceptions into API-friendly exceptions.
 *
 * Note: Between [fromBody] and [fromParam] there is some duplicate/similar code.
 * Please be mindful to update both instances when changing this code.
 */
internal class JacksonHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    e.findCause<BadRequestException>()?.findCause<JsonConvertException>()?.let { intermediary ->
      logger.info(e) { "Converting Jackson exception from body." }
      fromBody(intermediary)?.let { return it }
      logger.warn(e) { "No known way to convert Jackson exception from body." }
      return ExceptionResult.Exception(UnknownJsonError())
    }
    e.findCause<RestEndpointParamException>()?.let { intermediary ->
      logger.info(e) { "Converting Jackson exception from param." }
      fromParam(intermediary)?.let { return it }
      logger.warn(e) { "No known way to convert Jackson exception from param." }
      return ExceptionResult.Exception(UnknownJsonError())
    }
    return ExceptionResult.Unhandled
  }

  @Suppress("UnnecessaryLet")
  private fun fromBody(e: JsonConvertException): ExceptionResult? {
    fun result(
      constructor: KFunction2<String?, JsonBadRequestException.Location?, JsonBadRequestException>,
      cause: JsonMappingException,
    ): ExceptionResult.Exception {
      val path = JsonBadRequestException.parsePath(cause.path)
      val location = cause.location?.let { JsonBadRequestException.parseLocation(it) }
      return ExceptionResult.Exception(constructor(path, location))
    }

    fun result(constructor: KFunction0<JsonBadRequestException>): ExceptionResult.Exception =
      ExceptionResult.Exception(constructor())

    e.findCause<MissingKotlinParameterException>()?.let { cause ->
      return result(::MissingRequiredProperty, cause)
    }
    e.findCause<UnrecognizedPropertyException>()?.let { cause ->
      return result(::UnrecognizedProperty, cause)
    }
    e.findCause<IgnoredPropertyException>()?.let { cause ->
      return result(::UnrecognizedProperty, cause)
    }
    e.findCause<InvalidTypeIdException>()?.let { cause ->
      return result(::UnrecognizedPolymorphicType, cause)
    }
    e.findCause<JsonParseException>()?.let {
      return result(::MalformedJson)
    }
    e.findCause<JsonMappingException>()?.let { cause ->
      return result(::InvalidProperty, cause)
    }

    return null
  }

  @Suppress("UnnecessaryLet")
  private fun fromParam(e: RestEndpointParamException): ExceptionResult? {
    fun result(
      constructor: KFunction2<String?, JsonBadRequestException.Location?, JsonBadRequestException>,
    ): ExceptionResult.Exception {
      val path = e.name
      val location = null
      return ExceptionResult.Exception(constructor(path, location))
    }

    e.findCause<JsonMappingException>()?.let {
      return result(::InvalidProperty)
    }

    return null
  }
}
