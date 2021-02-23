package io.limberapp.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.limberapp.auth.JwtAuthVerifier
import io.limberapp.auth.auth
import io.limberapp.config.AuthenticationConfig
import io.limberapp.serialization.LimberObjectMapper

internal fun Application.configureAuthentication(
    authenticationConfig: AuthenticationConfig,
    objectMapper: LimberObjectMapper,
) {
  install(Authentication) {
    auth {
      verifier(
          scheme = JwtAuthVerifier.scheme,
          verifier = JwtAuthVerifier(authenticationConfig, objectMapper),
      )
    }
  }
}
