package kairo.sql

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getNamedInstance
import org.jdbi.v3.core.Jdbi

@Singleton
public class SqlTransactionProvider @Inject constructor(
  private val config: KairoSqlConfig,
  private val injector: Injector,
) : LazySingletonProvider<SqlTransaction>() {
  override fun create(): SqlTransaction {
    val jdbi = injector.getNamedInstance<Jdbi>(config.name)
    return SqlTransaction(jdbi)
  }
}
