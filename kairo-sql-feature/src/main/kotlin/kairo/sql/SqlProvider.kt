package kairo.sql

import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.namedKey
import kairo.transactionManager.TransactionManager

@Singleton
public class SqlProvider @Inject constructor(
  private val config: KairoSqlConfig,
  private val transactionManager: TransactionManager,
) : LazySingletonProvider<Sql>() {
  override fun create(): Sql {
    val sqlTransaction = namedKey<SqlTransaction>(config.name)
    return Sql(
      sqlTransaction = sqlTransaction,
      transactionManager = transactionManager,
    )
  }
}
