package io.limberapp.sql.store

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.jdbi.v3.core.statement.Update
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException

private val SQL_PATH_REGEX = Regex("store/[A-Za-z]+/[A-Za-z0-9]+\\.sql")

/**
 * An implementation of this class can provide SQL database access for a model.
 *
 * Query resources are fetched on-demand the first time they're needed, then stored indefinitely in
 * an in-memory cache.
 */
abstract class SqlStore(private val jdbi: Jdbi) {
  private val resourceCache = object : ResourceCache() {
    override fun get(resourceName: String): String {
      // For performance, this check is added to the resource cache rather than to the store itself.
      check(SQL_PATH_REGEX.matches(resourceName)) { "Invalid SQL path: $resourceName." }
      return super.get(resourceName)
    }
  }

  protected fun sqlResource(path: String): String = resourceCache[path]

  protected val UnableToExecuteStatementException.serverErrorMessage: ServerErrorMessage?
    get() {
      return when (val cause = cause) {
        is BatchUpdateException -> cause.cause as? PSQLException
        is PSQLException -> cause
        else -> null
      }?.serverErrorMessage
    }

  fun <R> withHandle(callback: (Handle) -> R): R = jdbi.withHandle<R, Exception>(callback)

  fun <R> inTransaction(callback: (Handle) -> R): R = jdbi.inTransaction<R, Exception>(callback)

  protected fun Update.update(): Unit? {
    return when (execute()) {
      0 -> null
      1 -> Unit
      else -> badSql()
    }
  }

  private fun badSql(): Nothing =
      error("An SQL statement invariant failed. The transaction has been aborted.")
}
