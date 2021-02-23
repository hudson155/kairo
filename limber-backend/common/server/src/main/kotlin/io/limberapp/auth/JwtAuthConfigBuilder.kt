package io.limberapp.auth

import io.ktor.auth.Principal

/**
 * Configuration for how to verify [JwtPrincipal]s using one or more [AuthVerifier]s. Each
 * [AuthVerifier] will verify [Principal]s with a certain "scheme", which is the prefix in the
 * Authorization header. For example, in "Authorization: Bearer: xxx", the scheme is "Bearer".
 */
internal class JwtAuthConfigBuilder {
  /**
   * All verifiers, with their schemes as keys.
   */
  internal val verifiers: MutableMap<String, AuthVerifier> = mutableMapOf()

  /**
   * Creates a verifier for the given scheme. Each scheme can only have one verifier. Exactly
   * one of the specified verifiers must be set as the default. The default verifier's scheme
   * is used for challenge responses.
   *
   * See https://en.wikipedia.org/wiki/Challenge–response_authentication for info about challenge responses.
   */
  fun verifier(scheme: String, verifier: AuthVerifier) {
    require(scheme !in verifiers) { "Can't set multiple verifiers with the same scheme." }
    verifiers[scheme] = verifier
  }
}
