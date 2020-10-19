package io.limberapp.monolith.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.ClockConfig
import io.limberapp.common.config.authentication.UuidsConfig
import io.limberapp.common.config.database.SqlDatabaseConfig

data class LimberMonolithConfig(
    override val monolithBaseUrl: String,
    override val authentication: AuthenticationConfig,
    override val clock: ClockConfig,
    val sqlDatabase: SqlDatabaseConfig,
    override val uuids: UuidsConfig,
) : Config
