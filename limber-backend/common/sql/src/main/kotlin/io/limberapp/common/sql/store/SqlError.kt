package io.limberapp.common.sql.store

import org.postgresql.util.ServerErrorMessage

fun ServerErrorMessage.isNotNullConstraintViolation(columnName: String): Boolean {
  if (sqlState != "23502") return false
  return column == columnName
}

fun ServerErrorMessage.isForeignKeyViolation(constraintName: String): Boolean {
  if (sqlState != "23503") return false
  return constraint == constraintName
}

fun ServerErrorMessage.isUniqueConstraintViolation(constraintName: String): Boolean {
  if (sqlState != "23505") return false
  return constraint == constraintName
}
