package kairo.rest.exceptionHandler

import io.ktor.http.BadContentTypeFormatException
import kairo.rest.exception.BadRequest
import kairo.rest.exception.MalformedContentType

/**
 * Converts Ktor-native exceptions into API-friendly exceptions.
 */
internal class KtorHandler : ExceptionHandler() {
  @Suppress("UnnecessaryLet")
  override fun handle(e: Throwable): ExceptionResult {
    if (e !is Exception) return ExceptionResult.Unhandled

    when (e) {
      is BadContentTypeFormatException -> {
        return ExceptionResult.Exception(MalformedContentType(e))
      }
      is io.ktor.server.plugins.BadRequestException -> {
        return ExceptionResult.Exception(BadRequest(e))
      }
      else -> {
        return ExceptionResult.Unhandled
      }
    }
  }
}
