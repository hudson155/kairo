package io.limberapp.monolith.config

import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig
import io.limberapp.config.database.SqlDatabaseConfig

data class LimberMonolithConfig(
  override val monolithBaseUrl: String?,
  override val authentication: AuthenticationConfig,
  override val clock: ClockConfig,
  val sqlDatabase: SqlDatabaseConfig,
  override val uuids: UuidsConfig,
) : Config
