package io.limberapp.backend.config

import io.limberapp.backend.config.database.DatabaseConfig
import io.limberapp.backend.config.jwt.JwtConfig

/**
 * The Config class contains all custom configuration for the app. It doesn't contain Ktor built-in
 * configuration.
 */
internal data class Config(
    val database: DatabaseConfig,
    val jwt: JwtConfig
)
