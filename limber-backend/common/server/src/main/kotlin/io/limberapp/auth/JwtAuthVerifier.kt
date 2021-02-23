package io.limberapp.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import io.limberapp.auth.exception.VerificationException
import io.limberapp.auth.jwt.Jwt
import io.limberapp.auth.jwt.JwtClaims
import io.limberapp.auth.jwt.JwtPrincipal
import io.limberapp.config.AuthenticationConfig
import io.limberapp.config.AuthenticationMechanism

/**
 * Verifies standard plaintext JWTs using the "Bearer" scheme.
 */
internal class JwtAuthVerifier(
    authenticationConfig: AuthenticationConfig,
    private val objectMapper: ObjectMapper,
) : AuthVerifier {
  companion object {
    const val scheme: String = "Bearer"
  }

  /**
   * [JwtVerifierProvider]s are keyed by issuer. The key is nullable since issuer is not a required
   * JWT claim.
   */
  private val providers: Map<String?, JwtVerifierProvider> =
      authenticationConfig.mechanisms.associate { mechanism ->
        val provider = when (mechanism) {
          is AuthenticationMechanism.Jwk -> JwtVerifierProvider.Jwk(
              domain = mechanism.domain,
              leeway = mechanism.leeway,
          )
          is AuthenticationMechanism.Jwt -> JwtVerifierProvider.Static(
              algorithm = mechanism.createAlgorithm(),
              leeway = mechanism.leeway,
          )
        }
        return@associate Pair(mechanism.issuer, provider)
      }

  override fun verify(authorizationHeader: String): JwtPrincipal {
    val decodedJwt = try {
      val verifier = getVerifierForIssuer(authorizationHeader)
          ?: throw VerificationException.unknownIssuer()
      verifier.verify(authorizationHeader)
    } catch (e: JWTVerificationException) {
      throw VerificationException.invalidJwt(e)
    }

    @Suppress("TooGenericExceptionCaught")
    val jwt = try {
      Jwt(
          permissions = decodedJwt.getClaim(JwtClaims.permissions)
              .let { objectMapper.convertValue(it.asString()) },
          org = decodedJwt.getClaim(JwtClaims.org).allowNull()
              ?.let { objectMapper.convertValue(it.asMap()) },
          features = decodedJwt.getClaim(JwtClaims.features).allowNull()
              ?.let { objectMapper.convertValue(it.asMap()) },
          user = decodedJwt.getClaim(JwtClaims.user).allowNull()
              ?.let { objectMapper.convertValue(it.asMap()) },
      )
    } catch (e: Exception) {
      throw VerificationException.invalidJwt(e)
    }
    return JwtPrincipal(jwt)
  }

  private fun getVerifierForIssuer(blob: String): JWTVerifier? {
    val jwt = JWT.decode(blob) // In order to determine the issuer we need to decode it first.
    // This results in double decoding. Performance could be improved by avoiding this.
    val provider = providers[jwt.issuer] ?: return null
    return provider[jwt.keyId]
  }

  private fun Claim.allowNull(): Claim? {
    if (this.isNull) return null
    return this
  }
}
