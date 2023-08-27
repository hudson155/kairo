package limber.feature.sql

import com.google.common.io.Resources
import com.google.inject.Inject
import kotlinx.coroutines.runBlocking
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException
import kotlin.reflect.KClass

public abstract class SqlStore<T : Any>(private val tableName: String, private val type: KClass<T>) {
  @Inject
  private lateinit var jdbi: Jdbi

  public suspend fun get(id: String): T? =
    get(id, forUpdate = false)

  protected suspend fun get(id: String, forUpdate: Boolean): T? =
    handle { handle ->
      val query = handle.createQuery(rs("store/common/get.sql"))
      query.define("tableName", tableName)
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("id", id)
      return@handle query.mapToType().singleNullOrThrow()
    }

  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()

  protected suspend fun <R> transaction(callback: suspend (Handle) -> R): R =
    jdbi.transaction(callback.withErrorHandling())

  protected suspend fun <R> handle(callback: suspend (Handle) -> R): R =
    jdbi.handle(callback)

  protected open fun ServerErrorMessage.onError(e: UnableToExecuteStatementException): Unit = Unit

  protected fun Query.mapToType(): ResultIterable<T> = mapTo(type.java)

  private fun <R> (suspend (Handle) -> R).withErrorHandling(): (suspend (Handle) -> R) = { handle ->
    try {
      this(handle)
    } catch (e: UnableToExecuteStatementException) {
      val message = e.serverErrorMessage() ?: throw e
      message.onError(e)
      throw e
    }
  }

  private fun UnableToExecuteStatementException.serverErrorMessage(): ServerErrorMessage? =
    when (val cause = cause) {
      is BatchUpdateException -> cause.cause as? PSQLException
      is PSQLException -> cause
      else -> null
    }?.serverErrorMessage
}

public fun <R> Jdbi.transaction(callback: suspend (Handle) -> R): R =
  inTransaction<R, Exception>(TransactionIsolationLevel.REPEATABLE_READ) { handle ->
    runBlocking {
      callback(handle)
    }
  }

public fun <R> Jdbi.handle(callback: suspend (Handle) -> R): R {
  return withHandle<R, Exception> { handle ->
    runBlocking {
      callback(handle)
    }
  }
}
