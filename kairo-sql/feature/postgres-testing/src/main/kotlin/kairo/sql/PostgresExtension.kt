package kairoSample.testing

import kairo.protectedString.ProtectedString
import kairo.sql.SqlFeatureConfig
import kotlin.uuid.Uuid
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer

@Suppress("SqlSourceToSinkFlow")
public class PostgresExtension : PostgresExtensionAware, BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
  /**
   * Idempotently starts a Postgres Docker container.
   */
  override fun beforeAll(context: ExtensionContext) {
    context.root.getStore(namespace).getOrComputeIfAbsent("postgres") {
      val postgres = PostgreSQLContainer("postgres:16.9")
      postgres.start()
      return@getOrComputeIfAbsent postgres
    }
  }

  /**
   * Creates a database for the current test.
   */
  @OptIn(ProtectedString.Access::class)
  override fun beforeEach(context: ExtensionContext) {
    val postgres = checkNotNull(context.postgres)
    val databaseName = Uuid.random().toString()
    context.databaseName = databaseName
    postgres.connection { connection ->
      connection.createStatement().executeUpdate("create database \"$databaseName\"")
    }
    context.database = Database.connect(
      url = postgres.jdbcUrl.replace("/${postgres.databaseName}", "/$databaseName"),
      user = postgres.username,
      password = postgres.password,
    )
    context.connectionFactory = SqlFeatureConfig.ConnectionFactory(
      url = postgres.jdbcUrl.replace("jdbc:", "r2dbc:").replace("/${postgres.databaseName}", "/$databaseName"),
      username = postgres.username,
      password = postgres.password.let { ProtectedString(it) },
    )
  }

  /**
   * Drops the database for the current test (if it exists).
   */
  override fun afterEach(context: ExtensionContext) {
    context.postgres?.connection { connection ->
      connection.createStatement().executeUpdate("drop database if exists \"${checkNotNull(context.databaseName)}\"")
    }
  }

  public companion object {
    public val namespace: ExtensionContext.Namespace =
      ExtensionContext.Namespace.create(PostgresExtension::class)
  }
}
