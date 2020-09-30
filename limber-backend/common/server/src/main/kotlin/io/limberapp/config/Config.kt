package io.limberapp.config

import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor built-in configuration.
 */
interface Config {
  val monolithBaseUrl: String?
  val authentication: AuthenticationConfig
  val clock: ClockConfig
  val uuids: UuidsConfig
}
