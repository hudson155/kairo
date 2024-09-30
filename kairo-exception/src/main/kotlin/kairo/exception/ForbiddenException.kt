package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class ForbiddenException(
  message: Nothing?,
  cause: Exception? = null,
) : KairoException(message, cause) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
}
