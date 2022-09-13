package limber.config

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import limber.config.deserializer.ConfigStringDeserializer
import limber.config.deserializer.ProtectedConfigStringDeserializer
import limber.type.ProtectedString

public data class SqlConfig(
  @JsonDeserialize(using = ConfigStringDeserializer::class)
  val jdbcUrl: String,
  @JsonDeserialize(using = ConfigStringDeserializer::class)
  val username: String? = null,
  @JsonDeserialize(using = ProtectedConfigStringDeserializer::class)
  val password: ProtectedString? = null,

  val migrations: Migrations,

  val connectionTimeoutMs: Long,
  val minimumIdle: Int,
  val maximumPoolSize: Int,
) {
  public data class Migrations(
    val run: Boolean,
    val cleanOnValidationError: Boolean,
    val locations: List<String>,
    val defaultSchema: String,
    val schemas: List<String>,
  )
}
