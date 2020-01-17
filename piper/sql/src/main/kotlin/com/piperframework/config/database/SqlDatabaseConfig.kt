package com.piperframework.config.database

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * This class encapsulates the configuration for the connection to an SQL database.
 */
data class SqlDatabaseConfig(
    val jdbcUrl: String,
    val username: String?,
    @JsonDeserialize(using = ConfigStringDeserializer::class)
    val password: String?
)
