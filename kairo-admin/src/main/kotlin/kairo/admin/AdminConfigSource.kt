package kairo.admin

/**
 * Represents a HOCON config source file to display in the admin dashboard.
 */
public data class AdminConfigSource(
  val name: String,
  val content: String,
)
