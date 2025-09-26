package kairo.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.protectedString.ProtectedString
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The SQL Feature uses Exposed to provide access to a SQL database.
 */
@Suppress("LongParameterList")
public class SqlFeature(
  config: SqlFeatureConfig,
  configureHikari: HikariConfig.() -> Unit = {},
  configureDatabase: DatabaseConfig.Builder.() -> Unit = {},
) : Feature(), HasKoinModules {
  override val name: String = "SQL"

  private val hikari: HikariDataSource =
    createHikari(
      connectionFactory = config.connectionFactory,
      connectionPool = config.connectionPool,
      block = configureHikari,
    )

  private val database: Database =
    createDatabase(
      hikari = hikari,
      config = config.database,
      block = configureDatabase,
    )

  override val koinModules: List<Module> =
    listOf(
      module {
        single<HikariDataSource> { hikari }
        single<Database> { database }
      },
    )

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler(FeaturePriority.database) {
        stop { hikari.close() }
      }
    }

  public companion object {
    public fun healthCheck(@Suppress("unused") hikari: HikariDataSource): Unit =
      Unit
  }
}

@OptIn(ProtectedString.Access::class)
private fun createHikari(
  connectionFactory: SqlFeatureConfig.ConnectionFactory,
  connectionPool: SqlFeatureConfig.ConnectionPool,
  block: HikariConfig.() -> Unit,
): HikariDataSource {
  val config = HikariConfig().apply {
    jdbcUrl = connectionFactory.url
    connectionFactory.username?.let { username = it }
    connectionFactory.password?.let { password = it.value }
    connectionTimeout = connectionFactory.connectTimeout.inWholeMilliseconds
    minimumIdle = connectionPool.size.min
    maximumPoolSize = connectionPool.size.max
    block()
  }
  return HikariDataSource(config)
}

private fun createDatabase(
  hikari: HikariDataSource,
  config: SqlFeatureConfig.Database,
  block: DatabaseConfig.Builder.() -> Unit,
): Database =
  Database.connect(
    datasource = hikari,
    databaseConfig = DatabaseConfig {
      defaultReadOnly = config.readOnly
      defaultMaxAttempts = config.maxAttempts
      block()
    },
  )
