package kairo.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import kairo.rest.exception.ExpiredJwt
import kairo.rest.exception.JwtVerificationFailed

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
    verifier = JWT.require(algorithm).acceptLeeway(leewaySec).build(), // Claim verification uses a real clock.
  )

  override fun verify(decodedJwt: DecodedJWT) {
    try {
      verifier.verify(decodedJwt)
    } catch (e: TokenExpiredException) {
      throw ExpiredJwt(e)
    } catch (e: JWTVerificationException) {
      throw JwtVerificationFailed(e)
    }
  }
}
