package io.limberapp.config

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val customProperty: String,
    val hosts: TestHosts,
    override val uuids: UuidsConfig,
) : Config
