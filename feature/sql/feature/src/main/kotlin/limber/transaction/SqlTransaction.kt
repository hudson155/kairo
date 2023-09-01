package limber.transaction

import com.google.inject.Inject
import limber.feature.sql.SqlContext
import limber.feature.sql.getSqlContext
import org.jdbi.v3.core.Jdbi
import kotlin.coroutines.CoroutineContext

public class SqlTransaction @Inject constructor(
  private val jdbi: Jdbi,
) : TransactionType(), TransactionType.WithContext {
  override suspend fun createContext(): CoroutineContext =
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
