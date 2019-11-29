package io.limberapp.backend.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.authentication.JwkAuthentication
import com.piperframework.config.authentication.JwtAuthentication
import com.piperframework.config.authentication.UnsignedJwtAuthentication
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.ktorAuth.PiperAuthVerifier
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt

class JwtAuthVerifier(authenticationConfig: AuthenticationConfig) : PiperAuthVerifier<Jwt> {

    private val objectMapper = PiperObjectMapper()

    private val providers = authenticationConfig.mechanisms.associate { mechanism ->
        val provider = when (mechanism) {
            is JwkAuthentication -> UrlJwtVerifierProvider(mechanism.domain)
            is JwtAuthentication -> StaticJwtVerifierProvider(JWT.require(Algorithm.HMAC256(mechanism.secret)).build())
            is UnsignedJwtAuthentication -> StaticJwtVerifierProvider(JWT.require(Algorithm.none()).build())
        }
        return@associate Pair(mechanism.issuer, provider)
    }

    override fun verify(blob: String): Jwt? {
        val decodedJwt = try {
            getVerifier(blob)?.verify(blob)
        } catch (_: JWTVerificationException) {
            null
        } ?: return null
        return Jwt(
            org = objectMapper.readValue(decodedJwt.getClaim(Claims.org).asString()),
            roles = objectMapper.readValue(decodedJwt.getClaim(Claims.roles).asString()),
            user = objectMapper.readValue(decodedJwt.getClaim(Claims.user).asString())
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
