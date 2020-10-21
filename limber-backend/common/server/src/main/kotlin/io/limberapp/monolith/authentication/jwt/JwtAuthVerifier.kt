package io.limberapp.monolith.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.module.kotlin.convertValue
import io.limberapp.backend.authorization.principal.JwtPrincipal
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.JwtClaims
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.AuthenticationMechanism
import io.limberapp.common.ktorAuth.LimberAuthVerifier
import io.limberapp.common.serialization.limberObjectMapper
import org.slf4j.LoggerFactory

class JwtAuthVerifier(authenticationConfig: AuthenticationConfig) : LimberAuthVerifier<JwtPrincipal> {
  private val logger = LoggerFactory.getLogger(JwtAuthVerifier::class.java)

  private val objectMapper = limberObjectMapper(allowUnknownProperties = true)

  private val providers = authenticationConfig.mechanisms.associate { mechanism ->
    val provider = when (mechanism) {
      is AuthenticationMechanism.Jwk -> UrlJwtVerifierProvider(
          domain = mechanism.domain,
          leeway = mechanism.leeway
      )
      is AuthenticationMechanism.Jwt -> StaticJwtVerifierProvider(
          jwtVerifier = JWT.require(mechanism.createAlgorithm()).acceptLeeway(mechanism.leeway).build()
      )
    }
    return@associate Pair(mechanism.issuer, provider)
  }

  override fun verify(blob: String): JwtPrincipal? {
    val decodedJwt = try {
      getVerifier(blob)?.verify(blob)
    } catch (e: JWTVerificationException) {
      logger.warn("JWT verification exception", e)
      null
    } ?: return null
    val jwt = Jwt(
        org = decodedJwt.getClaim(JwtClaims.org).asMap()
            ?.let { objectMapper.convertValue(it) },
        roles = requireNotNull(decodedJwt.getClaim(JwtClaims.roles).asList(String::class.java))
            .let { objectMapper.convertValue(it) },
        user = decodedJwt.getClaim(JwtClaims.user).asMap()
            ?.let { objectMapper.convertValue(it) }
    )
    return JwtPrincipal(jwt)
  }

  private fun getVerifier(blob: String): JWTVerifier? {
    val jwt = JWT.decode(blob)
    val provider = providers[jwt.issuer] ?: return null
    return provider[jwt.keyId]
  }

  companion object {
    const val scheme = "Bearer"
  }
}
