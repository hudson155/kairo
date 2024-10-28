package kairo.sql.util

import org.jdbi.v3.core.statement.SqlStatement

public fun <T : SqlStatement<T>> SqlStatement<T>.returningNothing(): SqlStatement<T> =
  returning(null)

public fun <T : SqlStatement<T>> SqlStatement<T>.returningStar(): SqlStatement<T> =
  returning("*")

private fun <T : SqlStatement<T>> SqlStatement<T>.returning(value: String?): SqlStatement<T> {
  val returning = value?.let { "returning $it" }.orEmpty()
  return apply {
    define("returning", returning)
  }
}
