package kairo.admin.model

public data class AdminDependencyInfo(
  val className: String,
  val simpleName: String,
  val packageName: String,
  val kind: String,
  val qualifier: String? = null,
  val scope: String? = null,
  val secondaryTypes: List<String> = emptyList(),
  val hasCallbacks: Boolean = false,
)
