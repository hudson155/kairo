package io.limberapp.framework.ktorAuth

import io.ktor.auth.AuthenticationProvider

class LimberAuthConfig internal constructor(name: String?) :
    AuthenticationProvider.Configuration(name) {

    internal val verifiers: MutableMap<String, LimberAuthVerifier> = mutableMapOf()

    var authKey = "LimberAuth"
    var realm = "Limber Server"

    fun verifier(scheme: String, verifier: LimberAuthVerifier) {
        require(!verifiers.containsKey(scheme))
        verifiers[scheme] = verifier
    }
}
