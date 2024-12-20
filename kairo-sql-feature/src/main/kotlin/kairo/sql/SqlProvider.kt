package kairo.sql

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.getNamedInstance
import kairo.dependencyInjection.namedKey
import kairo.transactionManager.TransactionManager

@Singleton
public class SqlProvider @Inject constructor(
  private val name: String,
) : LazySingletonProvider<Sql>() {
  @Inject
  private lateinit var injector: Injector

  private val config: KairoSqlConfig by lazy { injector.getNamedInstance(name) }
  private val transactionManager: TransactionManager by lazy { injector.getInstance() }

  override fun create(): Sql {
    val sqlTransaction = namedKey<SqlTransaction>(config.name)
    return Sql(
      sqlTransaction = sqlTransaction,
      transactionManager = transactionManager,
    )
  }
}
