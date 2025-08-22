package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MissingRequiredAuthHeader(
  private val headerName: String,
) : UnauthorizedException(
  message = "Missing a required auth-related header.",
) {
  override val response: Map<String, Any>
    get() = super.response + buildMap {
      put("headerName", headerName)
    }
}
