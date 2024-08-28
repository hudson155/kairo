package kairo.sql

import com.google.inject.Inject
import kairo.transactionManager.TransactionManager
import kairo.transactionManager.TransactionType
import kotlin.coroutines.CoroutineContext
import org.jdbi.v3.core.Jdbi

/**
 * Implementing SQL transactions manually allows this Feature to play nicely with [TransactionManager]
 */
public class SqlTransaction @Inject constructor(
  private val jdbi: Jdbi,
) : TransactionType(), TransactionType.WithContext {
  override suspend fun createContext(): SqlContext =
    SqlContext(jdbi.open())

  override suspend fun begin() {
    val context = checkNotNull(getSqlContext())
    context.handle.begin()
  }

  override suspend fun commit() {
    val context = checkNotNull(getSqlContext())
    context.handle.commit()
  }

  override suspend fun rollback() {
    val context = checkNotNull(getSqlContext())
    context.handle.rollback()
  }

  override suspend fun closeContext(context: CoroutineContext) {
    context as SqlContext
    context.handle.close()
  }
}
