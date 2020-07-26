package com.piperframework.store

import com.piperframework.util.singleNullOrThrow
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException

@Suppress("UnnecessaryAbstractClass")
abstract class SqlStore {
  private val resourceCache = ResourceCache()

  /**
   * Fetches the given SQL resource, throwing an exception if it does not exist. The implementation uses a cache that
   * never evicts.
   */
  protected fun sqlResource(name: String): String {
    val classResourcePath = checkNotNull(this::class.qualifiedName).replace('.', '/')
    val resourceName = "/$classResourcePath/$name.sql"
    return resourceCache.get(resourceName)
  }

  protected fun Query.asInt() = map { rs, _ ->
    rs.getInt(1).let { if (rs.wasNull()) null else it }
  }.singleNullOrThrow()

  protected val UnableToExecuteStatementException.serverErrorMessage: ServerErrorMessage?
    get() {
      val cause = cause
      return when (cause) {
        is BatchUpdateException -> cause.cause as? PSQLException
        is PSQLException -> cause
        else -> null
      }?.serverErrorMessage
    }

  /**
   * Returns a new empty conditions list and a new empty bindings map for JDBI template engine queries.
   */
  protected fun conditionsAndBindings(): Pair<MutableList<String>, MutableMap<String, Any>> {
    val conditions = mutableListOf<String>()
    val bindings = mutableMapOf<String, Any>()
    return Pair(conditions, bindings)
  }

  protected fun badSql(): Nothing = error("An SQL statement invariant failed. The transaction has been aborted.")
}
