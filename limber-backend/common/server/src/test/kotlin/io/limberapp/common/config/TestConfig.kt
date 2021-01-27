package io.limberapp.common.config

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val customProperty: String,
    override val hosts: TestHosts,
    override val shutDownTimeoutSeconds: Long,
    override val uuids: UuidsConfig,
) : Config
