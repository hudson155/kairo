package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class JwtVerificationFailed : LogicalFailure("JWT verification failed") {
  override val type: String = "JwtVerificationFailed"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
}
