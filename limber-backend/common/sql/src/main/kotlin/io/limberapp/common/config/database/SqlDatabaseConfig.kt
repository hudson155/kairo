package io.limberapp.common.config.database

import io.limberapp.common.config.ConfigString

/**
 * This class encapsulates the configuration for the connection to an SQL database.
 */
data class SqlDatabaseConfig(
  val jdbcUrl: ConfigString,
  val username: ConfigString,
  val password: ConfigString?,
  val connectionTimeout: Long? = null,
  val minimumIdle: Int? = null,
  val maximumPoolSize: Int? = null,
  val properties: Map<String, String> = emptyMap(),
)
