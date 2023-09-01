package limber.feature.sql

import com.google.inject.Inject
import limber.transaction.SqlTransaction
import limber.transaction.TransactionManager
import org.jdbi.v3.core.Handle

public class Sql @Inject constructor(
  private val transactionManager: TransactionManager,
) {
  @Suppress("MemberNameEqualsClassName")
  public suspend fun <T> sql(block: suspend (handle: Handle) -> T): T =
    transactionManager.transaction(SqlTransaction::class) {
      val context = checkNotNull(getSqlContext())
      return@transaction block(context.handle)
    }
}
