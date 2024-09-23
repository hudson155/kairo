package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class ForbiddenException : KairoException(null) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
}
