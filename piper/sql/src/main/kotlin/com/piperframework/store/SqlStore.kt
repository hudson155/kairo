package com.piperframework.store

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException

private val SQL_PATH_REGEX = Regex("/store/[A-Za-z]+/[A-Za-z0-9]+\\.sql")

@Suppress("UnnecessaryAbstractClass")
abstract class SqlStore(private val jdbi: Jdbi) {
  private val resourceCache = object : ResourceCache() {
    override fun get(resourceName: String): String {
      // This check is added to the resource cache rather than to the store itself for performance reasons.
      check(SQL_PATH_REGEX.matches(resourceName)) { "Invalid SQL path: $resourceName." }
      return super.get(resourceName)
    }
  }

  /**
   * Fetches the given SQL resource, throwing an exception if it does not exist. The implementation uses a cache that
   * never evicts.
   */
  protected fun sqlResource(path: String) = resourceCache.get(path)

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

  fun <R> inTransaction(callback: (Handle) -> R): R = jdbi.inTransaction<R, Exception>(callback)

  fun <R> withHandle(callback: (Handle) -> R): R = jdbi.withHandle<R, Exception>(callback)

  protected class QueryBuilder {
    val conditions = mutableListOf<String>()
    val bindings = mutableMapOf<String, Any>()
  }

  protected fun Query.build(build: QueryBuilder.() -> Unit): Query {
    val queryBuilder = QueryBuilder().apply { build() }
    return this
      .define("conditions", queryBuilder.conditions.joinToString(" AND "))
      .bindMap(queryBuilder.bindings)
  }

  protected fun badSql(): Nothing = error("An SQL statement invariant failed. The transaction has been aborted.")
}
