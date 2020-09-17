package io.limberapp.config

import io.limberapp.config.authentication.AuthenticationConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor built-in configuration.
 */
interface Config {
  val authentication: AuthenticationConfig
}
