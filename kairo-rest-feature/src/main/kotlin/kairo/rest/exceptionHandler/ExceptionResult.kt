package kairo.rest.exceptionHandler

import io.ktor.http.HttpStatusCode

@Suppress("ConvertObjectToDataObject")
internal sealed class ExceptionResult {
  internal object Unhandled : ExceptionResult()

  internal class Exception(val e: kotlin.Exception) : ExceptionResult()

  internal class Handled(val statusCode: HttpStatusCode, val response: Any) : ExceptionResult()
}
