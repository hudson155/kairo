package kairo.rest.exceptionHandler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Exception handling is completely managed by Kairo.
 * Ktor's built-in exception handling is not used at all.
 *
 * [ExceptionManager] determines how to respond to a given exception,
 * with help from various [ExceptionHandler] instances.
 *
 * Implementation note: [ExceptionHandler]s are evaluated sequentially.
 * This can lead to poor performance, and should be reconsidered later.
 *
 * Another implementation note: The list of [ExceptionHandler]s is not currently customizable,
 * but easily could be in the future.
 */
internal class ExceptionManager {
  private val handlers: List<ExceptionHandler> =
    listOf(
      JacksonHandler(),
      KtorHandler(),
      KairoHandler(),
    )

  fun handle(cause: Throwable): Pair<HttpStatusCode, Any?> {
    logger.debug(cause) { "Handling exception." }

    var e = cause

    handlers.forEach { handler ->
      when (val result = handler.handle(e)) {
        is ExceptionResult.Unhandled -> {
          // Unhandled exceptions are passed to the next exception handler.
        }
        is ExceptionResult.Exception -> {
          // These exceptions have been transformed.
          e = result.e
        }
        is ExceptionResult.Handled -> {
          // Handled exceptions cause a response and stop propagation.
          return@handle Pair(result.statusCode, result.response)
        }
      }
    }

    // If the exception wasn't handled by any of the exception handlers...
    logger.error(cause) { "Unhandled exception." }
    return Pair(HttpStatusCode.InternalServerError, null)
  }
}
