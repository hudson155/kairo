package com.piperframework.authentication

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.security.interfaces.RSAPublicKey

sealed class TokenVerifierProvider {
    abstract operator fun get(keyId: String?): TokenVerifier
}

class StaticJwtVerifierProvider(jwtVerifier: JWTVerifier) : TokenVerifierProvider() {
    private val tokenVerifier = JwtVerifier(jwtVerifier)
    override fun get(keyId: String?) = tokenVerifier
}

class UrlJwtVerifierProvider(domain: String) : TokenVerifierProvider() {

    private val jwkProvider = UrlJwkProvider(domain)

    override fun get(keyId: String?): TokenVerifier {
        val algorithm = jwkProvider.get(keyId).makeAlgorithm()
        return JwtVerifier(JWT.require(algorithm).build())
    }
}

@Suppress("UnsafeCast")
private fun Jwk.makeAlgorithm(): Algorithm = when (algorithm) {
    "RS256" -> Algorithm.RSA256(publicKey as RSAPublicKey, null)
    else -> throw IllegalArgumentException("Unsupported algorithm $algorithm")
}

