package kairo.sql

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getNamedInstance
import kairo.sql.plugin.jackson.JacksonPlugin
import kairo.sql.plugin.kairo.KairoPlugin
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin

@Singleton
public class JdbiProvider(
  private val name: String,
  private val configureJdbi: (jdbi: Jdbi) -> Unit,
) : LazySingletonProvider<Jdbi>() {
  @Inject
  private lateinit var injector: Injector

  private val dataSource: HikariDataSource by lazy { injector.getNamedInstance(name) }

  override fun create(): Jdbi =
    Jdbi.create(dataSource).apply { configureJdbi(this) }
}

public fun Jdbi.applyKairoPlugins() =
  apply {
    installPlugin(JacksonPlugin())
    installPlugin(KairoPlugin())
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())
  }
