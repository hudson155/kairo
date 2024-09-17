package kairo.sqlMigration

public data class KairoSqlMigrationConfig(
  val run: Boolean,
  val cleanOnValidationError: Boolean,
  val locations: List<String>,
  val defaultSchema: String,
  val schemas: List<String>,
  val createSchemas: Boolean,
  val tableName: String,
)
