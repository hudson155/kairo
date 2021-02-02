package io.limberapp.testing.integration

import io.limberapp.common.config.AuthenticationConfig
import io.limberapp.common.config.ClockConfig
import io.limberapp.common.config.Config
import io.limberapp.common.config.UuidsConfig

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
