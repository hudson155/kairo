package kairo.admin.model

public data class TableInfo(
  val schema: String,
  val name: String,
  val columns: List<ColumnInfo>,
)
