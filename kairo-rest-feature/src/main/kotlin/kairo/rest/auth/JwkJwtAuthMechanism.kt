package kairo.rest.auth

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import kairo.rest.exception.ExpiredJwt
import kairo.rest.exception.JwtVerificationFailed
import kairo.rest.exception.UnrecognizedJwtKeyId

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwkJwtAuthMechanism(
  override val issuers: List<String?>,
  private val verifiers: JwkVerifierProvider,
) : JwtAuthMechanism() {
  public constructor(
    issuers: List<String?>,
    jwksEndpoint: String,
    leewaySec: Long,
  ) : this(
    issuers = issuers,
    verifiers = JwkVerifierProvider(jwksEndpoint, leewaySec),
  )

  override fun verify(decodedJwt: DecodedJWT) {
    try {
      val verifier = verifiers[decodedJwt.keyId] ?: throw UnrecognizedJwtKeyId()
      verifier.verify(decodedJwt)
    } catch (e: TokenExpiredException) {
      throw ExpiredJwt(e)
    } catch (e: JWTVerificationException) {
      throw JwtVerificationFailed(e)
    }
  }
}
