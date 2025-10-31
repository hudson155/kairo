package kairo.sql

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.IsolationLevel
import io.r2dbc.spi.ValidationDepth
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.protectedString.ProtectedString
import kotlin.time.toJavaDuration
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The SQL Feature uses Exposed to provide access to a SQL database.
 */
public class SqlFeature(
  config: SqlFeatureConfig,
  configureConnectionFactory: ConnectionFactoryOptions.Builder.() -> Unit = {},
  configureConnectionPool: ConnectionPoolConfiguration.Builder.() -> Unit = {},
  configureDatabase: R2dbcDatabaseConfig.Builder.() -> Unit = {},
) : Feature(), HasKoinModules {
  override val name: String = "SQL"

  private val connectionPool: ConnectionPool =
    createConnectionPool(
      database = createConnectionFactory(
        config = config.connectionFactory,
        block = configureConnectionFactory,
      ),
      config = config.connectionPool,
      block = configureConnectionPool,
    )

  private val database: R2dbcDatabase =
    createDatabase(
      connectionPool = connectionPool,
      config = config.database,
      block = configureDatabase,
    )

  override val koinModules: List<Module> =
    listOf(
      module {
        single<ConnectionFactory> { connectionPool }
        single<R2dbcDatabase> { database }
      },
    )

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler(FeaturePriority.database) {
        start { connectionPool.warmup().awaitSingle() }
        stop { connectionPool.disposeLater().awaitFirstOrNull() }
      }
    }

  public companion object {
    @Suppress("ThrowingExceptionFromFinally")
    public suspend fun healthCheck(connectionFactory: ConnectionFactory) {
      val connection = connectionFactory.create().awaitSingle()
      var exception: Throwable? = null
      try {
        connection.validate(ValidationDepth.REMOTE).awaitSingle()
      } catch (e: Throwable) {
        exception = e
        throw e
      } finally {
        try {
          withContext(NonCancellable) {
            connection.close().awaitFirstOrNull()
          }
        } catch (closeException: Throwable) {
          if (exception == null) throw closeException
          exception.addSuppressed(closeException)
        }
      }
    }
  }
}

@OptIn(ProtectedString.Access::class)
private fun createConnectionFactory(
  config: SqlFeatureConfig.ConnectionFactory,
  block: ConnectionFactoryOptions.Builder.() -> Unit,
): ConnectionFactory {
  val options = ConnectionFactoryOptions.parse(config.url).mutate().apply {
    config.username?.let { option(ConnectionFactoryOptions.USER, it) }
    config.password?.let { option(ConnectionFactoryOptions.PASSWORD, it.value) }
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
