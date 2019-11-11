package io.limberapp.framework.config

import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.config.serving.ServingConfig

/**
 * The Config class contains all custom configuration for the app. It doesn't contain Ktor built-in
 * configuration.
 */
data class Config(
    val database: DatabaseConfig,
    val jwt: JwtConfig,
    val serving: ServingConfig
)
