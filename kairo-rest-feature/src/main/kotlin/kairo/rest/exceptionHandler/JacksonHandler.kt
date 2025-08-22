@file:Suppress("DEPRECATION", "ForbiddenImport")

package kairo.rest.exceptionHandler

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException
import com.fasterxml.jackson.databind.exc.InvalidNullException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import kairo.rest.exception.InvalidProperty
import kairo.rest.exception.JsonBadRequestException
import kairo.rest.exception.MalformedJson
import kairo.rest.exception.MissingRequiredProperty
import kairo.rest.exception.UnknownJsonError
import kairo.rest.exception.UnrecognizedPolymorphicType
import kairo.rest.exception.UnrecognizedProperty
import kairo.rest.reader.RestEndpointBodyException
import kairo.rest.reader.RestEndpointParamException

/**
 * Converts Jackson deserialization exceptions into API-friendly exceptions.
 */
internal class JacksonHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    if (e !is Exception) return ExceptionResult.Unhandled

    e.findCause<RestEndpointBodyException>()?.let { intermediary ->
      fromBody(intermediary, e)?.let { return@handle it }
      return@handle ExceptionResult.Exception(UnknownJsonError(e))
    }
    e.findCause<RestEndpointParamException>()?.let { intermediary ->
      fromParam(intermediary, e)?.let { return@handle it }
      return@handle ExceptionResult.Exception(UnknownJsonError(e))
    }

    return ExceptionResult.Unhandled
  }

  @Suppress("UnnecessaryLet")
  private fun fromBody(
    intermediary: RestEndpointBodyException,
    e: Exception,
  ): ExceptionResult.Exception? {
    intermediary.findCause<MissingKotlinParameterException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(MissingRequiredProperty(path, location, e))
    }
    intermediary.findCause<InvalidNullException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(MissingRequiredProperty(path, location, e))
    }
    intermediary.findCause<UnrecognizedPropertyException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(UnrecognizedProperty(path, location, e))
    }
    intermediary.findCause<IgnoredPropertyException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(UnrecognizedProperty(path, location, e))
    }
    intermediary.findCause<InvalidTypeIdException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(UnrecognizedPolymorphicType(path, location, e))
    }
    intermediary.findCause<JsonParseException>()?.let {
      return@fromBody ExceptionResult.Exception(MalformedJson(e))
    }
    intermediary.findCause<JsonMappingException>()?.let { cause ->
      val (path, location) = JsonBadRequestException.metadata(cause)
      return@fromBody ExceptionResult.Exception(InvalidProperty(path, location, e))
    }

    return null
  }

  @Suppress("UnnecessaryLet")
  private fun fromParam(
    intermediary: RestEndpointParamException,
    e: Exception,
  ): ExceptionResult.Exception? {
    intermediary.findCause<JsonMappingException>()?.let {
      return@fromParam ExceptionResult.Exception(InvalidProperty(intermediary.name, null, e))
    }

    return null
  }
}
