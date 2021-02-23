package io.limberapp.sql

import com.zaxxer.hikari.HikariDataSource
import io.limberapp.config.SqlDatabaseConfig
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class IntegrationTest {
  private val testSchemaName = "sqltest"

  private val config = SqlDatabaseConfig(
      jdbcUrl = "jdbc:postgresql://localhost/limber",
      defaultSchema = testSchemaName,
      username = System.getenv("LIMBER_TEST_POSTGRES_USERNAME") ?: "postgres",
      password = System.getenv("LIMBER_TEST_POSTGRES_PASSWORD"),
  )

  private lateinit var dataSource: HikariDataSource
  protected lateinit var jdbi: Jdbi
    private set

  @BeforeAll
  open fun beforeAll() {
    dataSource = config.createDataSource()
  }

  @BeforeEach
  open fun beforeEach() {
    jdbi = Jdbi.create(dataSource)
    createSchema()
  }

  @AfterAll
  open fun afterAll() {
    dataSource.close()
  }

  private fun createSchema() {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("CREATE SCHEMA IF NOT EXISTS $testSchemaName")
          .execute()
    }
  }
}
