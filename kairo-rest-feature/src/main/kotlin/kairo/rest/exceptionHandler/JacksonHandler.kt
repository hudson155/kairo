@file:Suppress("DEPRECATION", "ForbiddenImport")

package kairo.rest.exceptionHandler

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import kairo.rest.exception.MissingRequiredParameter
import kairo.rest.exception.UnrecognizedParameter
import kairo.rest.exception.WrongParameterType

/**
 * Converts Jackson deserialization exceptions into API-friendly exceptions.
 */
internal class JacksonHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    val jsonException = e.findCause<BadRequestException>()?.findCause<JsonConvertException>()
      ?: return ExceptionResult.Unhandled

    jsonException.directCause<MissingKotlinParameterException>()?.let { cause ->
      return ExceptionResult.Exception(MissingRequiredParameter.from(cause))
    }

    jsonException.directCause<UnrecognizedPropertyException>()?.let { cause ->
      return ExceptionResult.Exception(UnrecognizedParameter.from(cause))
    }

    jsonException.directCause<MismatchedInputException>()?.let { cause ->
      return ExceptionResult.Exception(WrongParameterType.from(cause))
    }

    return ExceptionResult.Unhandled
  }
}
