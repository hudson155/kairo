package kairo.sql

import com.google.inject.Inject
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.sql.plugin.jackson.JacksonPlugin
import kairo.sql.plugin.kairo.KairoPlugin
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin

@Singleton
public class JdbiProvider @Inject constructor(
  private val dataSource: HikariDataSource,
) : LazySingletonProvider<Jdbi>() {
  override fun create(): Jdbi =
    Jdbi.create(dataSource).apply {
      installPlugin(JacksonPlugin())
      installPlugin(KairoPlugin())
      installPlugin(KotlinPlugin())
      installPlugin(PostgresPlugin())
    }
}
