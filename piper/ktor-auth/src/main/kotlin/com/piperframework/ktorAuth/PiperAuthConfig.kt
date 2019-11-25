package com.piperframework.ktorAuth

import io.ktor.auth.AuthenticationProvider

class PiperAuthConfig private constructor(
    name: String?,
    val defaultScheme: String,
    val verifiers: Map<String, PiperAuthVerifier>,
    val authKey: String,
    val realm: String
) : AuthenticationProvider.Configuration(name) {

    class Builder(private val name: String?) {

        /**
         * The default scheme, used for challenge responses.
         *
         * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
         */
        private var defaultScheme: String? = null

        /**
         * All verifiers, with their schemes as keys.
         */
        private val verifiers: MutableMap<String, PiperAuthVerifier> = mutableMapOf()

        /**
         * The auth key used for challenge or error responses.
         *
         * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
         */
        var authKey = "PiperAuth"

        /**
         * The realm used for challenge responses.
         *
         * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
         */
        var realm = "Piper Server"

        /**
         * Creates a verifier for the given scheme. Each scheme can only have one verifier. Exactly
         * one of the specified verifiers must be set as the default. The default verifier's scheme
         * is used for challenge responses.
         *
         * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
         */
        fun verifier(scheme: String, verifier: PiperAuthVerifier, default: Boolean = false) {
            if (default) require(defaultScheme == null) { "Can't set multiple default verifiers." }
            require(!verifiers.containsKey(scheme)) { "Can't set multiple verifiers with the same scheme." }
            if (default) defaultScheme = scheme
            verifiers[scheme] = verifier
        }

        fun build(): PiperAuthConfig = PiperAuthConfig(
            name = name,
            defaultScheme = requireNotNull(defaultScheme),
            verifiers = verifiers,
            authKey = authKey,
            realm = realm
        )
    }
}
