package kairo.sql

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.IsolationLevel
import io.r2dbc.spi.ValidationDepth
import kairo.dependencyInjection.KoinModule
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import kotlin.time.toJavaDuration
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The SQL Feature uses Exposed to provide access to a SQL database.
 */
@Suppress("LongParameterList")
public class SqlFeature(
  config: SqlFeatureConfig,
  configureConnectionFactory: ConnectionFactoryOptions.Builder.() -> Unit = {},
  configureConnectionPool: ConnectionPoolConfiguration.Builder.() -> Unit = {},
  configureDatabase: R2dbcDatabaseConfig.Builder.() -> Unit = {},
) : Feature(), KoinModule {
  override val name: String = "SQL"

  private val connectionFactory: ConnectionFactory =
    createConnectionFactory(config.connectionFactory, configureConnectionFactory)

  private val connectionPool: ConnectionPool =
    createConnectionPool(connectionFactory, config.connectionPool, configureConnectionPool)

  private val database: R2dbcDatabase =
    createDatabase(connectionPool, config.database, configureDatabase)

  override val koinModule: Module =
    module {
      single { database }
    }

  override suspend fun start(features: List<Feature>) {
    suspendTransaction(db = database) {} // Establishes and immediately closes a connection.
    // TODO: Run migrations.
  }

  override suspend fun stop(features: List<Feature>) {
    connectionPool.disposeLater().awaitFirstOrNull()
  }

  public companion object
}

@OptIn(ProtectedString.Access::class)
private fun createConnectionFactory(
  config: SqlFeatureConfig.ConnectionFactory,
  block: ConnectionFactoryOptions.Builder.() -> Unit,
): ConnectionFactory {
  val options = ConnectionFactoryOptions.parse(config.url).mutate().apply {
    option(ConnectionFactoryOptions.USER, config.username)
    option(ConnectionFactoryOptions.PASSWORD, config.password.value)
    config.ssl?.let { option(ConnectionFactoryOptions.SSL, it) }
    option(ConnectionFactoryOptions.CONNECT_TIMEOUT, config.connectTimeout.toJavaDuration())
    option(ConnectionFactoryOptions.STATEMENT_TIMEOUT, config.statementTimeout.toJavaDuration())
    block()
  }.build()
  return ConnectionFactories.get(options)
}

private fun createConnectionPool(
  database: ConnectionFactory,
  config: SqlFeatureConfig.ConnectionPool,
  block: ConnectionPoolConfiguration.Builder.() -> Unit,
): ConnectionPool =
  ConnectionPool(
    ConnectionPoolConfiguration.builder().apply {
      connectionFactory(database)

      initialSize(config.size.initial)
      minIdle(config.size.min)
      maxSize(config.size.max)

      maxCreateConnectionTime(config.management.createConnectionTimeout.toJavaDuration())
      maxAcquireTime(config.management.acquireTimeout.toJavaDuration())
      acquireRetry(config.management.acquireAttempts - 1)
      maxLifeTime(config.management.maxLifetime.toJavaDuration())
      maxIdleTime(config.management.maxIdleTime.toJavaDuration())
      backgroundEvictionInterval(config.management.backgroundEvictionInterval.toJavaDuration())

      validationDepth(ValidationDepth.REMOTE)
      maxValidationTime(config.validation.timeout.toJavaDuration())

      block()
    }.build(),
  )

private fun createDatabase(
  connectionPool: ConnectionPool,
  config: SqlFeatureConfig.Database,
  block: R2dbcDatabaseConfig.Builder.() -> Unit,
): R2dbcDatabase =
  R2dbcDatabase.connect(
    connectionFactory = connectionPool,
    databaseConfig = R2dbcDatabaseConfig {
      defaultReadOnly = config.readOnly
      config.defaultIsolationLevel?.let { defaultR2dbcIsolationLevel = IsolationLevel.valueOf(it) }
      defaultMaxAttempts = config.maxAttempts
      block()
    },
  )
