package kairo.sql.store

import com.google.common.io.Resources
import com.google.inject.Inject
import com.google.inject.Injector
import java.sql.BatchUpdateException
import kairo.dependencyInjection.getNamedInstance
import kairo.reflect.KairoType
import kairo.sql.Sql
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage

/**
 * A SQL store provides access to the database.
 * This will typically be [SqlStore.ForType].
 *
 * Queries are done using [transaction].
 * Error handling is done using [onError] (global to the [SqlStore])
 * or by [transaction]'s argument.
 */
public abstract class SqlStore(databaseName: String) {
  @Inject
  private lateinit var injector: Injector

  private val sql: Sql by lazy { injector.getNamedInstance(databaseName) }

  /**
   * Use this to access [Handle] and make SQL queries.
   */
  protected suspend fun <T> transaction(
    onError: OnError = {},
    block: suspend (handle: Handle) -> T,
  ): T =
    sql.transaction inner@{ handle ->
      try {
        block(handle)
      } catch (e: UnableToExecuteStatementException) {
        val message = e.serverErrorMessage() ?: throw e
        onError(message)
        this.onError(message)
        throw e
      }
    }

  /**
   * Implement this to handle SQL errors. One implementation is shared across all methods.
   */
  protected open fun onError(message: ServerErrorMessage): Unit = Unit

  private fun UnableToExecuteStatementException.serverErrorMessage(): ServerErrorMessage? =
    when (val cause = cause) {
      is BatchUpdateException -> cause.cause as? PSQLException
      is PSQLException -> cause
      else -> null
    }?.serverErrorMessage

  public abstract class ForType<T : Any>(databaseName: String) : SqlStore(databaseName) {
    private val type: KairoType<T> = KairoType.from(ForType::class, 0, this::class)

    @Suppress("ForbiddenMethodCall", "UNCHECKED_CAST")
    protected fun Query.mapToType(): ResultIterable<T> =
      mapTo(type.javaType) as ResultIterable<T>
  }
}

/**
 * Call this to access a resource from the classpath, containing a SQL query.
 */
public fun rs(resourceName: String): String =
  Resources.getResource(resourceName).readText()
