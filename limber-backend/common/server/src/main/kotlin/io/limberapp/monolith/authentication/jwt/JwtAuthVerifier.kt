package io.limberapp.monolith.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.common.ktorAuth.LimberAuthVerifier
import io.limberapp.common.serialization.limberObjectMapper
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.AuthenticationMechanism
import org.slf4j.LoggerFactory

class JwtAuthVerifier(authenticationConfig: AuthenticationConfig) : LimberAuthVerifier<Jwt> {
  private val logger = LoggerFactory.getLogger(JwtAuthVerifier::class.java)

  private val objectMapper = limberObjectMapper()

  private val providers = authenticationConfig.mechanisms.associate { mechanism ->
    val provider = when (mechanism) {
      is AuthenticationMechanism.Jwk -> UrlJwtVerifierProvider(
          domain = mechanism.domain,
          leeway = mechanism.leeway
      )
      is AuthenticationMechanism.Jwt -> StaticJwtVerifierProvider(
          jwtVerifier = JWT.require(Algorithm.HMAC256(mechanism.secret)).acceptLeeway(mechanism.leeway).build()
      )
      is AuthenticationMechanism.UnsignedJwt -> StaticJwtVerifierProvider(
          jwtVerifier = JWT.require(Algorithm.none()).acceptLeeway(mechanism.leeway).build()
      )
    }
    return@associate Pair(mechanism.issuer, provider)
  }

  override fun verify(blob: String): Jwt? {
    val decodedJwt = try {
      getVerifier(blob)?.verify(blob)
    } catch (e: JWTVerificationException) {
      logger.warn("JWT verification exception", e)
      null
    } ?: return null
    return Jwt(
        org = decodedJwt.getClaim(Claims.org).asString()
            ?.let { objectMapper.readValue(it) },
        roles = requireNotNull(decodedJwt.getClaim(Claims.roles).asString())
            .let { objectMapper.readValue(it) },
        user = decodedJwt.getClaim(Claims.user).asString()
            ?.let { objectMapper.readValue(it) }
    )
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
