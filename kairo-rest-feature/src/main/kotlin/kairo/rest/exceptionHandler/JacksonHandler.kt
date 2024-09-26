@file:Suppress("DEPRECATION", "ForbiddenImport")

package kairo.rest.exceptionHandler

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import kairo.rest.exception.InvalidProperty
import kairo.rest.exception.MalformedJson
import kairo.rest.exception.MissingRequiredProperty
import kairo.rest.exception.UnrecognizedPolymorphicType
import kairo.rest.exception.UnrecognizedProperty

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Converts Jackson deserialization exceptions into API-friendly exceptions.
 */
internal class JacksonHandler : ExceptionHandler() {
  @Suppress("UnnecessaryLet")
  override fun handle(e: Throwable): ExceptionResult {
    val jsonException = e.findCause<BadRequestException>()?.findCause<JsonConvertException>()
      ?: return ExceptionResult.Unhandled
    logger.info(e) { "Converting Jackson exception." }

    jsonException.rootCause<MissingKotlinParameterException>()?.let { cause ->
      return ExceptionResult.Exception(MissingRequiredProperty.from(cause))
    }
    jsonException.rootCause<UnrecognizedPropertyException>()?.let { cause ->
      return ExceptionResult.Exception(UnrecognizedProperty.from(cause))
    }
    jsonException.rootCause<IgnoredPropertyException>()?.let { cause ->
      return ExceptionResult.Exception(UnrecognizedProperty.from(cause))
    }
    jsonException.rootCause<InvalidTypeIdException>()?.let { cause ->
      return ExceptionResult.Exception(UnrecognizedPolymorphicType.from(cause))
    }
    jsonException.rootCause<MismatchedInputException>()?.let { cause ->
      return ExceptionResult.Exception(InvalidProperty.from(cause))
    }
    jsonException.rootCause<JsonParseException>()?.let {
      return ExceptionResult.Exception(MalformedJson.create())
    }

    logger.warn(e) { "No known way to convert Jackson exception." }
    return ExceptionResult.Unhandled
  }
}
