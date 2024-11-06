package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class DuplicateHeader(
  private val headerName: String,
) : UnauthorizedException(
  message = "A duplicate header was detected.",
) {
  override val response: Map<String, Any>
    get() = super.response + buildMap {
      put("headerName", headerName)
    }
}
