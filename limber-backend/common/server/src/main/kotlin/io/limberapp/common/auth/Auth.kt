package io.limberapp.common.auth

import io.ktor.auth.Authentication

/**
 * This is used internally by Ktor, but never exposed to the end user.
 */
private const val AUTH_KEY: String = "LIMBER_AUTH_KEY"

internal fun Authentication.Configuration.auth(block: JwtAuthConfigBuilder.() -> Unit) {
  val config = JwtAuthConfigBuilder().apply(block)
  val provider = AuthProvider(config.verifiers, authKey = AUTH_KEY)
  register(provider)
}
