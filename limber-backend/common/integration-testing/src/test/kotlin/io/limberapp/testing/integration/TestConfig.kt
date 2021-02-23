package io.limberapp.testing.integration

import io.limberapp.config.AuthenticationConfig
import io.limberapp.config.ClockConfig
import io.limberapp.config.Config
import io.limberapp.config.UuidsConfig

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
