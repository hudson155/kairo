package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class NoJwt(
  cause: Throwable? = null,
) : LogicalFailure("No JWT", cause) {
  override val type: String = "NoJwt"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
}
