package kairo.exception

import io.ktor.http.HttpStatusCode

public abstract class TooManyRequestsException(
  message: String,
  cause: Exception? = null,
) : KairoException(message, cause) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.TooManyRequests
}
