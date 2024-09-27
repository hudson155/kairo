package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.KairoException

public class ContentTypeMismatch : KairoException(
  message = "This endpoint does not support the provided content type header.",
) {
  override val statusCode: HttpStatusCode = HttpStatusCode.UnsupportedMediaType
}
