package limber.config.sql

import limber.type.ProtectedString

public data class SqlConfig(
  val jdbcUrl: String,
  val username: String? = null,
  val password: ProtectedString? = null,

  val migrations: Migrations,

  val connectionTimeoutMs: Long,
  val minimumIdle: Int,
  val maximumPoolSize: Int,
) {
  public data class Migrations(
    val run: Boolean,
    val cleanOnValidationError: Boolean,
    val cleanDisabled: Boolean,
    val locations: List<String>,
    val defaultSchema: String,
    val schemas: List<String>,
  )
}
