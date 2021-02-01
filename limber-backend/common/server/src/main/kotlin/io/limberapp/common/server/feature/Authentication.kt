package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.limberapp.common.auth.JwtAuthVerifier
import io.limberapp.common.auth.auth
import io.limberapp.common.config.AuthenticationConfig
import io.limberapp.common.serialization.LimberObjectMapper

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
