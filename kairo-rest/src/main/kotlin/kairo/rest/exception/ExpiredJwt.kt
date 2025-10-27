package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class ExpiredJwt(
  cause: Throwable? = null,
) : LogicalFailure("Expired JWT", cause) {
  override val type: String = "ExpiredJwt"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
}
