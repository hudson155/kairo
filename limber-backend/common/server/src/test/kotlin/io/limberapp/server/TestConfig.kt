package io.limberapp.server

import io.limberapp.config.AuthenticationConfig
import io.limberapp.config.AuthenticationMechanism
import io.limberapp.config.ClockConfig
import io.limberapp.config.Config
import io.limberapp.config.UuidsConfig

internal object TestConfig : Config {
  override val authentication: AuthenticationConfig =
      AuthenticationConfig(listOf(AuthenticationMechanism.Jwt.Unsigned(leeway = 0)))
  override val clock: ClockConfig = ClockConfig(type = ClockConfig.Type.FIXED)
  override val uuids: UuidsConfig = UuidsConfig(generation = UuidsConfig.Generation.DETERMINISTIC)
}
