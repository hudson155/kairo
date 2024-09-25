@file:Suppress("DEPRECATION")

package kairo.rest.exceptionHandler

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.JsonConvertException
import kairo.rest.exception.MissingRequiredParameter

private val logger: KLogger = KotlinLogging.logger {}

internal class JacksonHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    val cause = e.findCause<JsonConvertException>()?.directCause<MissingKotlinParameterException>()
      ?: return ExceptionResult.Unhandled

    val path = derivePath(cause)

    return ExceptionResult.Exception(MissingRequiredParameter(path))
  }

  private fun derivePath(cause: MissingKotlinParameterException): String? {
    try {
      return cause.path.joinToString("") { reference ->
        buildString {
          when {
            reference.isNamed() -> append(".${reference.fieldName}")
            reference.isNumbered() -> append("[${reference.index}]")
            else -> error("Unsupported reference: $reference.")
          }
        }
      }.drop(1)
    } catch (e: Exception) {
      logger.error(e) { "Failed to derive path." }
      return null
    }
  }

  private fun JsonMappingException.Reference.isNamed(): Boolean =
    fieldName != null && index < 0

  private fun JsonMappingException.Reference.isNumbered(): Boolean =
    fieldName == null && index >= 0
}
