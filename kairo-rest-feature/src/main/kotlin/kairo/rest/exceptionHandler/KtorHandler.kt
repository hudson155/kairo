package kairo.rest.exceptionHandler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import kairo.rest.exception.BadRequest
import kairo.rest.exception.MalformedContentType

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Converts Ktor-native exceptions into API-friendly exceptions.
 */
internal class KtorHandler : ExceptionHandler() {
  @Suppress("UnnecessaryLet")
  override fun handle(e: Throwable): ExceptionResult {
    e.findCause<BadContentTypeFormatException>()?.let {
      logger.info(e) { "Converting Ktor exception." }
      return ExceptionResult.Exception(MalformedContentType())
    }
    e.findCause<io.ktor.server.plugins.BadRequestException>()?.let {
      logger.warn(e) { "Using fallback (no message) for Ktor exception." }
      return ExceptionResult.Exception(BadRequest())
    }

    return ExceptionResult.Unhandled
  }
}
