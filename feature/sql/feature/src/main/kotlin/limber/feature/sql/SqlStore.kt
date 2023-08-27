package limber.feature.sql

import com.google.common.io.Resources
import com.google.inject.Inject
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException
import kotlin.reflect.KClass

public abstract class SqlStore<T : Any>(private val tableName: String, private val type: KClass<T>) {
  @Inject
  private lateinit var sql: Sql

  public suspend fun get(id: String): T? =
    get(id, forUpdate = false)

  protected suspend fun get(id: String, forUpdate: Boolean): T? =
    sql { handle ->
      val query = handle.createQuery(rs("store/common/get.sql"))
      query.define("tableName", tableName)
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("id", id)
      return@sql query.mapToType().singleNullOrThrow()
    }

  protected suspend fun <T> sql(block: suspend (Handle) -> T): T =
    sql.sql inner@{ handle ->
      try {
        return@inner block(handle)
      } catch (e: UnableToExecuteStatementException) {
        val message = e.serverErrorMessage() ?: throw e
        message.onError(e)
        throw e
      }
    }

  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()

  protected open fun ServerErrorMessage.onError(e: UnableToExecuteStatementException): Unit = Unit

  protected fun Query.mapToType(): ResultIterable<T> = mapTo(type.java)

  private fun UnableToExecuteStatementException.serverErrorMessage(): ServerErrorMessage? =
    when (val cause = cause) {
      is BatchUpdateException -> cause.cause as? PSQLException
      is PSQLException -> cause
      else -> null
    }?.serverErrorMessage
}
