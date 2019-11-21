package io.limberapp.framework.util

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTAuthenticationProvider
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.limberapp.framework.config.authentication.AuthenticationConfig
import io.limberapp.framework.config.authentication.JwkAuthentication
import io.limberapp.framework.config.authentication.JwtAuthentication
import io.limberapp.framework.config.authentication.UnsignedJwtAuthentication

internal fun Authentication.Configuration.configureAuthentication(
    authenticationConfig: AuthenticationConfig
) {

    authenticationConfig.mechanisms.forEach { mechanism ->
        when (mechanism) {
            is JwkAuthentication -> {
                jwt {
                    verifier(UrlJwkProvider(mechanism.domain))
                    validateJwtPrincipal()
                }
            }
            is JwtAuthentication -> {
                jwt {
                    verifier(JWT.require(Algorithm.HMAC256(mechanism.secret)).build())
                    validateJwtPrincipal()
                }
            }
            is UnsignedJwtAuthentication -> {
                jwt {
                    verifier(JWT.require(Algorithm.none()).build())
                    validateJwtPrincipal()
                }
            }
        }
    }
}

private fun JWTAuthenticationProvider.Configuration.validateJwtPrincipal() {
    validate { credential -> JWTPrincipal(credential.payload) }
}

