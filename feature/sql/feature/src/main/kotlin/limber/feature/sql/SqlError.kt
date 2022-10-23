package limber.feature.sql

import org.postgresql.util.ServerErrorMessage

/**
 * Refer to https://www.postgresql.org/docs/current/errcodes-appendix.html for the error codes used here.
 */

public fun ServerErrorMessage.isNotNullViolation(columnName: String): Boolean {
  if (sqlState != "23502") return false
  return column == columnName
}

public fun ServerErrorMessage.isForeignKeyViolation(constraintName: String): Boolean {
  if (sqlState != "23503") return false
  return constraint == constraintName
}

public fun ServerErrorMessage.isUniqueViolation(constraintName: String): Boolean {
  if (sqlState != "23505") return false
  return constraint == constraintName
}
