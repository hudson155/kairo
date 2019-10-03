package io.limberapp.backend.config

import io.limberapp.backend.config.database.DatabaseConfig

/**
 * The Config class contains all custom configuration for the app. It doesn't contain Ktor built-in
 * configuration.
 */
internal data class Config(
    val database: DatabaseConfig
)
