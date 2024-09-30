package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class UnauthorizedException(
  cause: Exception? = null,
) : KairoException(null, cause) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
}
