package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class ForbiddenException(
  cause: Exception? = null,
) : KairoException(null, cause) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
}
