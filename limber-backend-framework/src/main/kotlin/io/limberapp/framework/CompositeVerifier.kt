package io.limberapp.framework

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.auth.HttpAuthHeader
import io.limberapp.framework.config.authentication.AuthenticationConfig
import io.limberapp.framework.config.authentication.JwkAuthentication
import io.limberapp.framework.config.authentication.JwtAuthentication
import io.limberapp.framework.config.authentication.UnsignedJwtAuthentication
import java.security.interfaces.RSAPublicKey

private interface JwtVerifierProvider {
    operator fun get(keyId: String?): JWTVerifier
}

private class StaticJwtVerifierProvider(
    private val jwtVerifier: JWTVerifier
) : JwtVerifierProvider {
    override fun get(keyId: String?) = jwtVerifier
}

private class UrlJwtVerifierProvider(domain: String) : JwtVerifierProvider {

    val jwkProvider = UrlJwkProvider(domain)

    override fun get(keyId: String?): JWTVerifier {
        val algorithm = jwkProvider.get(keyId).makeAlgorithm()
        return JWT.require(algorithm).build()
    }
}

class LimberJwtVerifierProvider(authenticationConfig: AuthenticationConfig) {

    private val providers = authenticationConfig.mechanisms.associate { mechanism ->
        val provider = when (mechanism) {
            is JwkAuthentication ->
                UrlJwtVerifierProvider(mechanism.domain)
            is JwtAuthentication ->
                StaticJwtVerifierProvider(JWT.require(Algorithm.HMAC256(mechanism.secret)).build())
            is UnsignedJwtAuthentication ->
                StaticJwtVerifierProvider(JWT.require(Algorithm.none()).build())
        }
        return@associate Pair(mechanism.issuer, provider)
    }

    fun getVerifier(header: HttpAuthHeader): JWTVerifier? {
        if (header !is HttpAuthHeader.Single) return null
        if (header.authScheme.toLowerCase() != "bearer") return null
        val jwt = JWT.decode(header.blob)
        val provider = providers[jwt.issuer] ?: return null
        return provider[jwt.keyId]
    }
}

private fun Jwk.makeAlgorithm(): Algorithm = when (algorithm) {
    "RS256" -> Algorithm.RSA256(publicKey as RSAPublicKey, null)
    else -> throw IllegalArgumentException("Unsupported algorithm $algorithm")
}
