package kairo.sqlMigration

import com.google.inject.Inject
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import org.flywaydb.core.Flyway

@Singleton
internal class FlywayProvider @Inject constructor(
  private val config: KairoSqlMigrationConfig,
  private val dataSource: HikariDataSource,
) : LazySingletonProvider<Flyway>() {
  @Suppress("SpreadOperator") // The spread operator causes a full array copy, which is fine at startup time.
  override fun create(): Flyway =
    Flyway.configure()
      .locations(*config.locations.toTypedArray())
      .defaultSchema(config.defaultSchema)
      .schemas(*config.schemas.toTypedArray())
      .table(config.tableName)
      .dataSource(dataSource)
      .baselineVersion("0")
      .baselineOnMigrate(true)
      .createSchemas(config.createSchemas)
      .load()
}
