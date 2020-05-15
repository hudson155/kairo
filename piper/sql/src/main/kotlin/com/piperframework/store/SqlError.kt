package com.piperframework.store

import org.postgresql.util.ServerErrorMessage

/**
 * Returns true if the server error message represents a foreign key violation on the given constraint.
 */
fun ServerErrorMessage.isForeignKeyViolation(constraintName: String): Boolean {
  if (sqlState != "23503") return false
  return constraint == constraintName
}

/**
 * Returns true if the server error message represents a unique constraint violation on the given constraint.
 */
fun ServerErrorMessage.isUniqueConstraintViolation(constraintName: String): Boolean {
  if (sqlState != "23505") return false
  return constraint == constraintName
}
