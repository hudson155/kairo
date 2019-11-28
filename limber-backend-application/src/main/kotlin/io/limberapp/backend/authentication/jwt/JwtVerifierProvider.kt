package io.limberapp.backend.authentication.jwt

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.security.interfaces.RSAPublicKey

internal sealed class JwtVerifierProvider {
    abstract operator fun get(keyId: String?): JWTVerifier
}

internal class StaticJwtVerifierProvider(private val jwtVerifier: JWTVerifier) : JwtVerifierProvider() {
    override fun get(keyId: String?) = jwtVerifier
}

internal class UrlJwtVerifierProvider(domain: String) : JwtVerifierProvider() {

    private val jwkProvider = UrlJwkProvider(domain)

    override fun get(keyId: String?): JWTVerifier {
        val algorithm = jwkProvider.get(keyId).makeAlgorithm()
        return JWT.require(algorithm).build()
    }
}

@Suppress("UnsafeCast")
private fun Jwk.makeAlgorithm(): Algorithm = when (algorithm) {
    "RS256" -> Algorithm.RSA256(publicKey as RSAPublicKey, null)
    else -> throw IllegalArgumentException("Unsupported algorithm $algorithm")
}
