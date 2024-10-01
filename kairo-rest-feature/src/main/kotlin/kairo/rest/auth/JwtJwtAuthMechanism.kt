package kairo.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import kairo.protectedString.ProtectedString

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
internal class JwtJwtAuthMechanism(
  override val issuers: List<String?>,
  private val verifier: JWTVerifier,
) : JwtAuthMechanism() {
  override fun verify(decodedJwt: DecodedJWT) {
    verifier.verify(decodedJwt)
  }

  internal companion object {
    @OptIn(ProtectedString.Access::class)
    fun from(config: JwtAuthMechanismConfig.Jwt): JwtJwtAuthMechanism {
      val algorithm = when (val algorithm = config.algorithm) {
        is JwtAuthMechanismConfig.Jwt.Algorithm.Hmac256 -> Algorithm.HMAC256(algorithm.secret.value)
      }
      return JwtJwtAuthMechanism(
        issuers = config.issuers,
        verifier = JWT.require(algorithm).acceptLeeway(config.leewaySec).build(),
      )
    }
  }
}
