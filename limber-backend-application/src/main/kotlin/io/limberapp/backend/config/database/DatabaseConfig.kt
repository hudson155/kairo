package io.limberapp.backend.config.database

/**
 * This class encapsulates the configuration for the connection to MongoDB. The class name does not
 * specify that the database is MongoDB, so if/when multiple databases are introduced, this should
 * be renamed.
 */
internal data class DatabaseConfig(
    val host: String,
    val database: String,
    val user: String,
    val password: String
)
