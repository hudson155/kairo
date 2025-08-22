package kairo.rest.auth

import io.ktor.server.auth.AuthenticationConfig

public fun AuthenticationConfig.kairoConfigure(block: MutableList<AuthVerifier<*>>.() -> Unit) {
  val provider = KairoAuthenticationProvider(buildList(block))
  register(provider)
}
