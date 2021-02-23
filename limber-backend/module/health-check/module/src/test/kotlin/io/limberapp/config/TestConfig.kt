package io.limberapp.config

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    override val uuids: UuidsConfig,
) : Config
