package io.limberapp.framework.ktorAuth

import com.auth0.jwt.JWTVerifier
import io.ktor.auth.Authentication
import io.ktor.http.auth.HttpAuthHeader

fun Authentication.Configuration.limberAuth(verifier: (HttpAuthHeader) -> JWTVerifier?) {
    register(JWTAuthenticationProvider(verifier))
}
