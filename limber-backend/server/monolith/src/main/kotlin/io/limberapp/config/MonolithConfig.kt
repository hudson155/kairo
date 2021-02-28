package io.limberapp.config

internal data class MonolithConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val hosts: Hosts,
    val sqlDatabase: SqlDatabaseConfig,
    override val uuids: UuidsConfig,
) : Config
