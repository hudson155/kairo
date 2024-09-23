package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class BadRequestException(message: String) : KairoException(message) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
}
