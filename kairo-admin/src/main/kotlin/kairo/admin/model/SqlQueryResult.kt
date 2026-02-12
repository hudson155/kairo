package kairo.admin.model

public data class SqlQueryResult(
  val columns: List<String>,
  val rows: List<List<String?>>,
  val rowCount: Int,
  val executionTimeMs: Long,
  val error: String? = null,
)
