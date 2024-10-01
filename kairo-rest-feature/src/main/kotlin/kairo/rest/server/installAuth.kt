package kairo.rest.server

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import kairo.rest.auth.AuthVerifier
import kairo.rest.auth.KairoAuthenticationProvider

internal fun Application.installAuth(
  verifiers: List<AuthVerifier>,
) {
  val provider = KairoAuthenticationProvider(verifiers)

  install(Authentication) {
    register(provider)
  }
}
