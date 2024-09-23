package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class UnauthorizedException : KairoException(null) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
}
