package limber.sql

import com.google.common.io.Resources
import com.google.inject.Inject
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

public abstract class SqlStore<T : Any>(private val type: KClass<T>) {
  @Inject
  private lateinit var jdbi: Jdbi

  @Suppress("FunctionMinLength") // Short for the purpose of brevity due to frequent reuse.
  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()

  protected fun <R> transaction(callback: (Handle) -> R): R =
    jdbi.transaction(callback.withErrorHandling())

  protected fun <R> handle(callback: (Handle) -> R): R =
    jdbi.handle(callback)

  protected open fun ServerErrorMessage.onError(e: UnableToExecuteStatementException): Unit = Unit

  protected fun Query.mapToType(): ResultIterable<T> = mapTo(type.java)

  private fun <R> ((Handle) -> R).withErrorHandling(): ((Handle) -> R) = { handle ->
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

public fun <R> Jdbi.transaction(callback: (Handle) -> R): R =
  inTransaction<R, Exception>(TransactionIsolationLevel.REPEATABLE_READ, callback)

public fun <R> Jdbi.handle(callback: (Handle) -> R): R =
  withHandle<R, Exception>(callback)
