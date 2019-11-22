package io.limberapp.framework.ktorAuth

import com.auth0.jwt.JWTVerifier
import io.ktor.auth.AuthenticationProvider
import io.ktor.http.auth.HttpAuthHeader

class LimberAuthConfig internal constructor(name: String?) :
    AuthenticationProvider.Configuration(name) {

    var authKey = "LimberAuth"
    var realm = "Limber Server"
    var verifier: (HttpAuthHeader) -> JWTVerifier? = { null }
}
