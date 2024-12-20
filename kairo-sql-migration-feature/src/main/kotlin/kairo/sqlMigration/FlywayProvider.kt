package kairo.sqlMigration

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getNamedInstance
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import org.flywaydb.core.Flyway

/**
 * Cleaning the database can only be enabled by enabling [KairoSqlMigrationConfig.cleanOnValidationError]
 * AND setting KAIRO_CLEAN_DATABASE to true.
 * This is a protective measure to avoid accidentally cleaning production databases.
 */
@Singleton
internal class FlywayProvider(
  private val name: String,
) : LazySingletonProvider<Flyway>() {
  @Inject
  private lateinit var injector: Injector

  private val config: KairoSqlMigrationConfig by lazy { injector.getNamedInstance(name) }
  private val dataSource: HikariDataSource by lazy { injector.getNamedInstance(name) }

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
