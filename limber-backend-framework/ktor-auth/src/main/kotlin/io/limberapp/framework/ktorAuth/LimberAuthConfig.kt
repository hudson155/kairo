package io.limberapp.framework.ktorAuth

import io.ktor.auth.AuthenticationProvider

class LimberAuthConfig private constructor(
    name: String?,
    val defaultScheme: String,
    val verifiers: Map<String, LimberAuthVerifier>,
    val authKey: String,
    val realm: String
) : AuthenticationProvider.Configuration(name) {

    class Builder(private val name: String?) {

        /**
         * The default scheme.
         */
        private var defaultScheme: String? = null

        /**
         * All verifiers, with their schemes as keys.
         */
        private val verifiers: MutableMap<String, LimberAuthVerifier> = mutableMapOf()

        /**
         * The auth key used for challenge or error responses. See
         * https://en.wikipedia.org/wiki/Challenge–response_authentication. I don't really
         * understand this, but it's how it works.
         */
        var authKey = "LimberAuth"

        /**
         * The realm used for challenge responses. See
         * https://en.wikipedia.org/wiki/Challenge–response_authentication. I don't really
         * understand this, but it's how it works.
         */
        var realm = "Limber Server"

        /**
         * Creates a verifier for the given scheme. Each scheme can only have one verifier. Exactly
         * one of the specified verifiers must be set as the default. The default verifier's scheme
         * is used for challenge responses.
         */
        fun verifier(scheme: String, verifier: LimberAuthVerifier, default: Boolean = false) {
            if (default) require(defaultScheme == null) { "Can't set multiple default verifiers." }
            require(!verifiers.containsKey(scheme)) { "Can't set multiple verifiers with the same scheme." }
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
