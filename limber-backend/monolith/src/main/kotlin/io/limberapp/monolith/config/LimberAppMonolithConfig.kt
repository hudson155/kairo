package io.limberapp.monolith.config

import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.database.SqlDatabaseConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class LimberAppMonolithConfig(
  val sqlDatabase: SqlDatabaseConfig,
  override val authentication: AuthenticationConfig,
) : Config
