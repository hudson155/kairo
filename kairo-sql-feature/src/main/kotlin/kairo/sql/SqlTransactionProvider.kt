package kairo.sql

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getNamedInstance
import org.jdbi.v3.core.Jdbi

@Singleton
public class SqlTransactionProvider(
  private val name: String,
) : LazySingletonProvider<SqlTransaction>() {
  @Inject
  private lateinit var injector: Injector

  private val jdbi: Jdbi by lazy { injector.getNamedInstance(name) }

  override fun create(): SqlTransaction =
    SqlTransaction(jdbi)
}
