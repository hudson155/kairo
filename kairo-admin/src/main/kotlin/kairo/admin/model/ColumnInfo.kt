package kairo.admin.model

public data class ColumnInfo(
  val name: String,
  val dataType: String,
  val isNullable: Boolean,
  val defaultValue: String?,
  val maxLength: Int?,
)
