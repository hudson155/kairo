package com.piperframework.config.database

/**
 * This class encapsulates the configuration for the connection to an SQL database.
 */
data class SqlDatabaseConfig(
    val jdbcUrl: String,
    val username: String?
)
