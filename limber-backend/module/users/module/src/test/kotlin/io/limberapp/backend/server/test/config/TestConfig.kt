package io.limberapp.backend.server.test.config

import io.limberapp.backend.config.SqlDatabaseConfig
import io.limberapp.common.config.AuthenticationConfig
import io.limberapp.common.config.ClockConfig
import io.limberapp.common.config.Config
import io.limberapp.common.config.UuidsConfig

internal data class TestConfig(
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val sqlDatabase: SqlDatabaseConfig,
    override val uuids: UuidsConfig,
) : Config
