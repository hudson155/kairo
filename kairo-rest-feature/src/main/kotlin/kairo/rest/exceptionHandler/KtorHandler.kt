@file:Suppress("ForbiddenImport")

package kairo.rest.exceptionHandler

import io.ktor.http.BadContentTypeFormatException
import io.ktor.server.plugins.BadRequestException
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
      is BadRequestException -> {
        return ExceptionResult.Exception(BadRequest(e))
      }
      else -> {
        return ExceptionResult.Unhandled
      }
    }
  }
}
