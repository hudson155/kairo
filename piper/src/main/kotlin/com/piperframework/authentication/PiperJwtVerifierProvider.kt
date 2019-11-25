package com.piperframework.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.authentication.JwkAuthentication
import com.piperframework.config.authentication.JwtAuthentication
import com.piperframework.config.authentication.UnsignedJwtAuthentication

class PiperJwtVerifierProvider(authenticationConfig: AuthenticationConfig) {

    private val providers = authenticationConfig.mechanisms.associate { mechanism ->
        val provider = when (mechanism) {
            is JwkAuthentication -> UrlJwtVerifierProvider(mechanism.domain)
            is JwtAuthentication -> StaticJwtVerifierProvider(JWT.require(Algorithm.HMAC256(mechanism.secret)).build())
            is UnsignedJwtAuthentication -> StaticJwtVerifierProvider(JWT.require(Algorithm.none()).build())
        }
        return@associate Pair(mechanism.issuer, provider)
    }

    fun getVerifier(blob: String): JWTVerifier? {
        val jwt = JWT.decode(blob)
        val provider = providers[jwt.issuer] ?: return null
        return provider[jwt.keyId]
    }
}
