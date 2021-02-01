package io.limberapp.common.server

import io.limberapp.common.config.AuthenticationConfig
import io.limberapp.common.config.AuthenticationMechanism
import io.limberapp.common.config.ClockConfig
import io.limberapp.common.config.Config
import io.limberapp.common.config.Hosts
import io.limberapp.common.config.UuidsConfig

internal object TestConfig : Config {
  override val authentication: AuthenticationConfig =
      AuthenticationConfig(listOf(AuthenticationMechanism.Jwt.Unsigned(leeway = 0)))
  override val clock: ClockConfig = ClockConfig(type = ClockConfig.Type.FIXED)
  override val hosts: Hosts = TestHosts
  override val uuids: UuidsConfig = UuidsConfig(generation = UuidsConfig.Generation.DETERMINISTIC)
}
