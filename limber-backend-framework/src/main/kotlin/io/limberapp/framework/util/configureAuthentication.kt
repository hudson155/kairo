package io.limberapp.framework.util

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.limberapp.framework.config.jwt.JwtConfig

internal fun Authentication.Configuration.configureAuthentication(jwtConfig: JwtConfig) {
    jwt {
        with(jwtConfig) {
            when {
                requireSignature && domain != null && secret == null ->
                    verifier(UrlJwkProvider(domain))
                requireSignature && domain == null && secret != null ->
                    verifier(JWT.require(Algorithm.HMAC256(secret)).build())
                !requireSignature && domain == null && secret == null ->
                    verifier(JWT.require(Algorithm.none()).build())
                else -> error("Invalid JWT config")
            }
        }
        validate { credential -> JWTPrincipal(credential.payload) }
    }
}

