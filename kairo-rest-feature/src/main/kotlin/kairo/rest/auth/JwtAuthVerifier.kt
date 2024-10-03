package kairo.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.auth.HttpAuthHeader
import kairo.rest.exception.MalformedJwt
import kairo.rest.exception.UnrecognizedJwtIssuer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwtAuthVerifier(
  override val schemes: List<String>,
  private val mechanisms: Map<String?, JwtAuthMechanism>,
) : AuthVerifier<JwtPrincipal>() {
  public constructor(
    schemes: List<String>,
    mechanisms: List<JwtAuthMechanism>,
  ) : this(
    schemes = schemes,
    mechanisms = mechanisms
      .flatMap { mechanism -> mechanism.issuers.map { Pair(it, mechanism) } }
      .toMap(),
  )

  override fun verify(authHeader: HttpAuthHeader.Single): JwtPrincipal {
    logger.debug { "Verifying scheme: ${authHeader.authScheme}." }
    val decodedJwt = decodeJwt(authHeader)
    val mechanism = getMechanism(decodedJwt) ?: throw UnrecognizedJwtIssuer()
    mechanism.verify(decodedJwt)
    return JwtPrincipal(decodedJwt)
  }

  private fun decodeJwt(authHeader: HttpAuthHeader.Single): DecodedJWT {
    try {
      return JWT.decode(authHeader.blob)
    } catch (e: JWTDecodeException) {
      throw MalformedJwt(e)
    }
  }

  private fun getMechanism(decodedJwt: DecodedJWT): JwtAuthMechanism? =
    mechanisms[decodedJwt.issuer]
}
