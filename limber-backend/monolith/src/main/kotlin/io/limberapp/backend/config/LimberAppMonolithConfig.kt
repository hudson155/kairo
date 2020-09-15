package io.limberapp.backend.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.database.SqlDatabaseConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class LimberAppMonolithConfig(
  val sqlDatabase: SqlDatabaseConfig,
  override val authentication: AuthenticationConfig,
) : Config
