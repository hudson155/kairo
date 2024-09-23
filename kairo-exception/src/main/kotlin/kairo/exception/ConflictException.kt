package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class ConflictException(message: String) : KairoException(message) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.Conflict
}
