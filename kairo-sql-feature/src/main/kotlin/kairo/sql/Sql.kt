package kairo.sql

import com.google.inject.Key
import kairo.sql.store.SqlStore
import kairo.transactionManager.TransactionManager
import org.jdbi.v3.core.Handle

/**
 * This wrapper is useful both for injecting into [SqlStore]
 * and for using SQL transactions from outside stores.
 */
public class Sql(
  private val sqlTransaction: Key<SqlTransaction>,
  private val transactionManager: TransactionManager,
) {
  public suspend fun <T> sql(block: suspend (handle: Handle) -> T): T =
    transactionManager.transaction(sqlTransaction) {
      val context = checkNotNull(getSqlContext())
      return@transaction block(context.handle)
    }
}
