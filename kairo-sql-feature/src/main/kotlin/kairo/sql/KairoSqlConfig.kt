package kairo.sql

import kairo.protectedString.ProtectedString

public data class KairoSqlConfig(
  val name: String,
  val jdbcUrl: String,
  val schema: String?,
  val username: String?,
  val password: ProtectedString?,
  val properties: Map<String, String>,
  val connectionTimeoutMs: Long,
  val minimumIdle: Int,
  val maximumPoolSize: Int,
)
