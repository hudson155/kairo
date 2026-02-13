package kairo.admin.sql

/**
 * Validates that SQL queries are read-only.
 */
public class SqlValidator {
  private val forbiddenKeywords: Set<String> = setOf(
    "INSERT",
    "UPDATE",
    "DELETE",
    "DROP",
    "ALTER",
    "CREATE",
    "TRUNCATE",
    "GRANT",
    "REVOKE",
    "COPY",
    "CALL",
    "EXECUTE",
  )

  public fun validate(sql: String): String? {
    val trimmed = stripComments(sql).trim()
    if (trimmed.isEmpty()) {
      return "Query cannot be empty."
    }
    val firstKeyword = trimmed.split("\\s+".toRegex()).firstOrNull()?.uppercase() ?: return "Invalid query."
    if (firstKeyword in forbiddenKeywords) {
      return "Only read-only queries are allowed. '$firstKeyword' statements are not permitted."
    }
    val allowedFirstKeywords = setOf("SELECT", "WITH", "EXPLAIN", "SHOW")
    if (firstKeyword !in allowedFirstKeywords) {
      return "Only SELECT, WITH, EXPLAIN, and SHOW statements are allowed. Got '$firstKeyword'."
    }
    return null
  }

  private fun stripComments(sql: String): String =
    sql
      .replace(Regex("--[^\n]*"), "")
      .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")
}
