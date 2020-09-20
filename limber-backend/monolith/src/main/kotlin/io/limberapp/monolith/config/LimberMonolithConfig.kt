package io.limberapp.monolith.config

import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig
import io.limberapp.config.database.SqlDatabaseConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class LimberMonolithConfig(
  override val authentication: AuthenticationConfig,
  override val clock: ClockConfig,
  val sqlDatabase: SqlDatabaseConfig,
  override val uuids: UuidsConfig,
) : Config
