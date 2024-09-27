package kairo.rest.exceptionHandler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.exception.KairoException

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Handles exceptions native to Kairo.
 */
internal class KairoHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    if (e !is KairoException) return ExceptionResult.Unhandled
    logger.info(e) { "${e.statusCode} Kairo Exception: ${e.response}." }
    return ExceptionResult.Handled(e.statusCode, e.response)
  }
}
