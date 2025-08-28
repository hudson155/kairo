package kairo.sql

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.IsolationLevel
import kairo.dependencyInjection.KoinModule
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import kotlin.time.toJavaDuration
import kotlinx.coroutines.reactive.awaitSingle
import org.jetbrains.exposed.v1.core.Schema
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.koin.core.module.Module

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

  override fun Module.koin() {
    single { database }
  }

  override suspend fun start(features: List<Feature>) {
    suspendTransaction(db = database) {} // Establishes and immediately closes a connection.
    // TODO: Run migrations.
  }

  override suspend fun stop(features: List<Feature>) {
    connectionPool.disposeLater().awaitSingle()
  }
}

@OptIn(ProtectedString.Access::class)
private fun createConnectionFactory(
  config: SqlFeatureConfig.ConnectionFactory,
  block: ConnectionFactoryOptions.Builder.() -> Unit,
): ConnectionFactory {
  val options = ConnectionFactoryOptions.parse(config.url).mutate().apply {
    option(ConnectionFactoryOptions.USER, config.username)
    option(ConnectionFactoryOptions.PASSWORD, config.password.value)
    option(ConnectionFactoryOptions.SSL, config.ssl)
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
      acquireRetry(config.acquireAttempts - 1)
      backgroundEvictionInterval(config.backgroundEvictionInterval.toJavaDuration())
      initialSize(config.initialSize)
      maxAcquireTime(config.maxAcquireTime.toJavaDuration())
      maxCreateConnectionTime(config.maxCreateConnectionTime.toJavaDuration())
      maxIdleTime(config.maxIdleTime.toJavaDuration())
      maxValidationTime(config.maxValidationTime.toJavaDuration())
      connectionFactory(database)
      validationQuery("select 1;")
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
      config.schema?.let { defaultSchema = Schema(it) }
      config.defaultIsolationLevel?.let { defaultR2dbcIsolationLevel = IsolationLevel.valueOf(it) }
      defaultReadOnly = config.readOnly
      defaultMaxAttempts = config.maxRetries
      block()
    },
  )
