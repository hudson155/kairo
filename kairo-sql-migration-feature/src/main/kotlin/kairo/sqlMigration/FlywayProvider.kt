package kairo.sqlMigration

import com.google.inject.Inject
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import org.flywaydb.core.Flyway

/**
 * Cleaning the database can only be enabled by enabling [KairoSqlMigrationConfig.cleanOnValidationError]
 * AND setting KAIRO_CLEAN_DATABASE to true.
 * This is a protective measure to avoid accidentally cleaning production databases.
 */
@Singleton
internal class FlywayProvider @Inject constructor(
  private val config: KairoSqlMigrationConfig,
  private val dataSource: HikariDataSource,
) : LazySingletonProvider<Flyway>() {
  @Suppress("SpreadOperator") // The spread operator causes a full array copy, which is fine at startup time.
  override fun create(): Flyway =
    Flyway.configure()
      .cleanOnValidationError(config.cleanOnValidationError)
      .cleanDisabled(DefaultEnvironmentVariableSupplier["KAIRO_CLEAN_DATABASE"] != true.toString())
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
