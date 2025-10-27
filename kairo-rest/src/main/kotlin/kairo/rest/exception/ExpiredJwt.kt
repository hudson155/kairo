package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class ExpiredJwt : LogicalFailure("Expired JWT") {
  override val type: String = "ExpiredJwt"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
}
