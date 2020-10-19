package io.limberapp.common.config

import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.ClockConfig
import io.limberapp.common.config.authentication.UuidsConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor built-in configuration.
 */
interface Config {
  val monolithBaseUrl: String
  val authentication: AuthenticationConfig
  val clock: ClockConfig
  val uuids: UuidsConfig
}
