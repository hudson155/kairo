package kairo.rest.exceptionHandler

import io.ktor.http.BadContentTypeFormatException
import kairo.rest.exception.MalformedContentType

internal class BadContentTypeFormatHandler : ExceptionHandler() {
  override fun handle(e: Throwable): ExceptionResult {
    if (e !is BadContentTypeFormatException) return ExceptionResult.Unhandled
    return ExceptionResult.Exception(MalformedContentType())
  }
}
