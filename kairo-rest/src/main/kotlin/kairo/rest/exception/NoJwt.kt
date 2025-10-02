package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class NoJwt : LogicalFailure() {
  override val type: String = "NoJwt"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
  override val title: String = "No JWT"
}
