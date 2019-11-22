package io.limberapp.framework.ktorAuth

import io.ktor.auth.AuthenticationProvider

class LimberAuthConfig private constructor(
    name: String?,
    val defaultScheme: String,
    val verifiers: Map<String, LimberAuthVerifier>,
    val authKey: String,
    val realm: String
) :
    AuthenticationProvider.Configuration(name) {

    class Builder(private val name: String?) {

        var defaultScheme: String? = null
        private val verifiers: MutableMap<String, LimberAuthVerifier> = mutableMapOf()

        var authKey = "LimberAuth"

        var realm = "Limber Server"

        fun verifier(scheme: String, verifier: LimberAuthVerifier, default: Boolean = false) {
            if (default) require(defaultScheme == null)
            require(!verifiers.containsKey(scheme))
            if (default) defaultScheme = scheme
            verifiers[scheme] = verifier
        }

        fun build(): LimberAuthConfig = LimberAuthConfig(
            name = name,
            defaultScheme = requireNotNull(defaultScheme),
            verifiers = verifiers,
            authKey = authKey,
            realm = realm
        )
    }
}
