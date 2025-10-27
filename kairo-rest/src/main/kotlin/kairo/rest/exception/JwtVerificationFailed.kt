package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class JwtVerificationFailed(
  cause: Throwable? = null,
) : LogicalFailure("JWT verification failed", cause) {
  override val type: String = "JwtVerificationFailed"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
}
