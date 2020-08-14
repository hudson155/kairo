package com.piperframework.ktorAuth

import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Principal

/**
 * Configuration for how to verify [Principal]s using one or more [PiperAuthVerifier]s.
 */
class PiperAuthConfig<P : Principal> private constructor(
  name: String?,
  val defaultScheme: String,
  val verifiers: Map<String, PiperAuthVerifier<P>>,
  val authKey: String,
  val realm: String,
) : AuthenticationProvider.Configuration(name) {
  class Builder<P : Principal>(private val name: String?) {
    /**
     * The default scheme, used for challenge responses.
     *
     * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
     */
    private var defaultScheme: String? = null

    /**
     * All verifiers, with their schemes as keys.
     */
    private val verifiers: MutableMap<String, PiperAuthVerifier<P>> = mutableMapOf()

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
    fun verifier(scheme: String, verifier: PiperAuthVerifier<P>, default: Boolean = false) {
      if (default) require(defaultScheme == null) { "Can't set multiple default verifiers." }
      require(scheme !in verifiers) { "Can't set multiple verifiers with the same scheme." }
      if (default) defaultScheme = scheme
      verifiers[scheme] = verifier
    }

    fun build(): PiperAuthConfig<P> = PiperAuthConfig(
      name = name,
      defaultScheme = requireNotNull(defaultScheme),
      verifiers = verifiers,
      authKey = authKey,
      realm = realm
    )
  }
}
