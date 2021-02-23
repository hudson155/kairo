package io.limberapp.config

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val sqlDatabase: SqlDatabaseConfig,
    override val uuids: UuidsConfig,
) : Config
