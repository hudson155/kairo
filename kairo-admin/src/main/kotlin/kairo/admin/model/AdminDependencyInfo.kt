package kairo.admin.model

public data class AdminDependencyInfo(
  val className: String,
  val kind: String,
  val qualifier: String? = null,
)
