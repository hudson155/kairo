package kairo.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwtJwtAuthMechanism(
  override val issuers: List<String?>,
  private val verifier: JWTVerifier,
) : JwtAuthMechanism() {
  public constructor(
    issuers: List<String?>,
    algorithm: Algorithm,
    leewaySec: Long,
  ) : this(
    issuers = issuers,
    verifier = JWT.require(algorithm).acceptLeeway(leewaySec).build(),
  )

  override fun verify(decodedJwt: DecodedJWT) {
    verifier.verify(decodedJwt)
  }
}
