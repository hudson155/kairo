package kairo.sql

import com.google.inject.Injector
import com.google.inject.Key
import kairo.sql.store.SqlStore
import kairo.transactionManager.TransactionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

/**
 * This wrapper is useful both for injecting into [SqlStore]
 * and for using SQL transactions from outside stores.
 */
@Suppress("LongParameterList")
public class Sql(
  private val dispatcher: CoroutineDispatcher,
  private val injector: Injector,
  private val jdbi: Jdbi,
  private val sqlTransaction: Key<SqlTransaction>,
  private val transactionManager: TransactionManager,
) {
  public suspend fun <T> transaction(block: suspend (handle: Handle) -> T): T =
    withContext(dispatcher) {
      return@withContext transactionManager.transaction(listOf(injector.getInstance(sqlTransaction))) {
        val context = checkNotNull(getSqlContext())
        return@transaction block(context.handle)
      }
    }

  public suspend fun <T> sql(block: suspend (handle: Handle) -> T): T =
    withContext(dispatcher) {
      return@withContext jdbi.open().use { handle ->
        return@use block(handle)
      }
    }
}
