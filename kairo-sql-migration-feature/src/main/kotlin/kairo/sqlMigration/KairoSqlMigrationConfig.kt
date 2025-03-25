package kairo.sqlMigration

public data class KairoSqlMigrationConfig(
  val additionalProperties: Map<String, String?> = emptyMap(),
  val name: String,
  val run: Boolean,
  val cleanOnValidationError: Boolean,
  val locations: List<String>,
  val defaultSchema: String,
  val schemas: List<String>,
  val createSchemas: Boolean,
  val tableName: String,
)
