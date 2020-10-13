package io.limberapp.common.ktorAuth

import io.ktor.auth.Authentication
import io.ktor.auth.Principal

fun <P : Principal> Authentication.Configuration.limberAuth(
    name: String? = null,
    block: LimberAuthConfig.Builder<P>.() -> Unit,
) {
  val config = LimberAuthConfig.Builder<P>(name).apply(block)
  val provider = LimberAuthProvider(config.build())
  register(provider)
}
