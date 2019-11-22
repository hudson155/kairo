package io.limberapp.framework.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.limberapp.framework.ktorAuth.StaticJwtVerifierProvider
import io.limberapp.framework.ktorAuth.UrlJwtVerifierProvider
import io.ktor.http.auth.HttpAuthHeader
import io.limberapp.framework.config.authentication.AuthenticationConfig
import io.limberapp.framework.config.authentication.JwkAuthentication
import io.limberapp.framework.config.authentication.JwtAuthentication
import io.limberapp.framework.config.authentication.UnsignedJwtAuthentication

class LimberJwtVerifierProvider(authenticationConfig: AuthenticationConfig) {

    private val providers = authenticationConfig.mechanisms.associate { mechanism ->
        val provider = when (mechanism) {
            is JwkAuthentication ->
                UrlJwtVerifierProvider(mechanism.domain)
            is JwtAuthentication ->
                StaticJwtVerifierProvider(
                    JWT.require(
                        Algorithm.HMAC256(
                            mechanism.secret
                        )
                    ).build()
                )
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
