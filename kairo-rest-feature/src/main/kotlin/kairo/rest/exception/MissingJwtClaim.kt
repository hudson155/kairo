package kairo.rest.exception

import kairo.exception.UnauthorizedException

public class MissingJwtClaim(
  private val claimName: String,
) : UnauthorizedException(
  message = "A claim is missing from the JWT.",
) {
  override val response: Map<String, Any>
    get() = super.response + buildMap {
      put("claimName", claimName)
    }
}
