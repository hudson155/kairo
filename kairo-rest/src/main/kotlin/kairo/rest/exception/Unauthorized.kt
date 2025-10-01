package kairo.rest.exception

import io.ktor.http.HttpStatusCode
import kairo.exception.LogicalFailure

public class Unauthorized : LogicalFailure() {
  override val type: String = "Unauthorized"
  override val status: HttpStatusCode = HttpStatusCode.Unauthorized
  override val title: String = "Unauthorized"
}
