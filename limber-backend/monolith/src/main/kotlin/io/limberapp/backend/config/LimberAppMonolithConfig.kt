package io.limberapp.backend.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.database.SqlDatabaseConfig
import io.limberapp.common.config.hashing.HashingConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class LimberAppMonolithConfig(
  val sqlDatabase: SqlDatabaseConfig,
  override val authentication: AuthenticationConfig,
  override val hashing: HashingConfig,
) : Config
