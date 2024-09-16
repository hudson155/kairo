package kairo.sql

import com.google.common.io.Resources
import com.google.inject.Inject
import java.sql.BatchUpdateException
import kairo.id.KairoId
import kairo.reflect.typeParam
import kairo.sql.SqlStore.ForTable
import kairo.sql.SqlStore.ForType
import kotlin.reflect.KClass
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage

/**
 * A SQL store provides access to the database.
 * This will typically be [ForTable], or at least [ForType].
 *
 * Queries are done using [sql].
 * Error handling is done using [onError].
 */
public abstract class SqlStore {
  @Inject
  private lateinit var sql: Sql

  /**
   * Use this to access [Handle] and make SQL queries.
   */
  protected suspend fun <T> sql(block: suspend (handle: Handle) -> T): T =
    sql.sql inner@{ handle ->
      try {
        return@inner block(handle)
      } catch (e: UnableToExecuteStatementException) {
        val message = e.serverErrorMessage() ?: throw e
        message.onError(e)
        throw e
      }
    }

  /**
   * Implement this to handle SQL errors. One implementation is shared across all methods.
   */
  protected open fun ServerErrorMessage.onError(e: UnableToExecuteStatementException): Unit = Unit

  private fun UnableToExecuteStatementException.serverErrorMessage(): ServerErrorMessage? =
    when (val cause = cause) {
      is BatchUpdateException -> cause.cause as? PSQLException
      is PSQLException -> cause
      else -> null
    }?.serverErrorMessage

  /**
   * Call this to access a resource from the classpath, containing a SQL query.
   */
  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()

  public abstract class ForType<T : Any> : SqlStore() {
    private val type: KClass<T> = typeParam(ForType::class, 0, this::class)

    protected fun Query.mapToType(): ResultIterable<T> =
      mapTo(type)
  }

  public abstract class ForTable<T : Any>(private val tableName: String) : ForType<T>() {
    public suspend fun get(id: KairoId): T? =
      get(id, forUpdate = false)

    protected suspend fun get(id: KairoId, forUpdate: Boolean): T? =
      sql { handle ->
        val query = handle.createQuery(rs("store/sql/get.sql"))
        query.define("tableName", tableName)
        query.define("lockingClause", if (forUpdate) "for no key update" else "")
        query.bind("id", id)
        return@sql query.mapToType().singleNullOrThrow()
      }

    protected suspend fun <U : Any> update(id: KairoId, updater: (model: T) -> U): U? {
      val model = get(id, forUpdate = true) ?: return null
      return updater(model)
    }
  }
}
