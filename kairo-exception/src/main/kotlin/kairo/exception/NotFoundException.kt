package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class NotFoundException(message: String) : KairoException(message) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.NotFound
}
