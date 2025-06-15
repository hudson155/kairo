package kairo.sql

import kairo.transactionManager.TransactionManager
import kairo.transactionManager.TransactionType
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import org.jdbi.v3.core.Jdbi

/**
 * Implementing SQL transactions manually allows this Feature to play nicely with [TransactionManager].
 */
public class SqlTransaction(
  private val jdbi: Jdbi,
) : TransactionType(), TransactionType.WithContext {
  override suspend fun createContext(): SqlContext =
    SqlContext(jdbi.open())

  override suspend fun begin() {
    val context = checkNotNull(coroutineContext[SqlContext])
    context.handle.begin()
  }

  override suspend fun commit() {
    val context = checkNotNull(coroutineContext[SqlContext])
    context.handle.commit()
  }

  override suspend fun rollback() {
    val context = checkNotNull(coroutineContext[SqlContext])
    context.handle.rollback()
  }

  override suspend fun closeContext(context: CoroutineContext) {
    context as SqlContext
    context.handle.close()
  }
}
