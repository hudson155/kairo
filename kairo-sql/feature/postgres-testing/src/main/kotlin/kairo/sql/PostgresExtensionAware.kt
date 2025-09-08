package kairoSample.testing

import java.sql.Connection
import java.sql.DriverManager
import kairo.sql.SqlFeature
import kairo.sql.SqlFeatureConfig
import kairoSample.testing.PostgresExtension.Companion.namespace
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer

public interface PostgresExtensionAware {
  public var ExtensionContext.connectionFactory: SqlFeatureConfig.ConnectionFactory?
    get() = getStore(namespace).get<SqlFeatureConfig.ConnectionFactory>("connectionFactory")
    set(value) {
      getStore(namespace).put("connectionFactory", value)
    }

  public var ExtensionContext.database: Database?
    get() = getStore(namespace).get<Database>("database")
    set(value) {
      getStore(namespace).put("database", value)
    }

  public var ExtensionContext.databaseName: String?
    get() = getStore(namespace).get<String>("databaseName")
    set(value) {
      getStore(namespace).put("databaseName", value)
    }

  public val ExtensionContext.postgres: PostgreSQLContainer<*>?
    get() = root.getStore(namespace).get<PostgreSQLContainer<*>>("postgres")

  public fun <T> PostgreSQLContainer<*>.connection(block: (connection: Connection) -> T): T =
    DriverManager.getConnection(jdbcUrl, username, password).use(block)
}

public fun SqlFeature.Companion.from(connectionFactory: SqlFeatureConfig.ConnectionFactory): SqlFeature =
  SqlFeature(
    config = SqlFeatureConfig(
      connectionFactory = connectionFactory,
      connectionPool = SqlFeatureConfig.ConnectionPool(
        size = SqlFeatureConfig.ConnectionPool.Size(initial = 2, min = 1, max = 5),
      ),
    ),
    configureDatabase = {
      explicitDialect = PostgreSQLDialect()
    },
  )
