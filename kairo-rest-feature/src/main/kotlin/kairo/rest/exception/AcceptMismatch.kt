package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.KairoException

public class AcceptMismatch : KairoException(
  message = "This endpoint does not support the provided accept header.",
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.NotAcceptable
}
