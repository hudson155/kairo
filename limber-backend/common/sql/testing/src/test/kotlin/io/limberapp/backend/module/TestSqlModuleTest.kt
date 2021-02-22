package io.limberapp.backend.module

import io.limberapp.common.sql.IntegrationTest
import io.limberapp.common.sql.SqlWrapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Suppress("SameParameterValue")
internal class TestSqlModuleTest : IntegrationTest() {
  private val testTableName = "test_sql_module_test"
  private val sqlWrapper = SqlWrapper(config)
  private val module = TestSqlModule(sqlWrapper, schemaName = testSchemaName)

  @BeforeAll
  override fun beforeAll() {
    super.beforeAll()
    sqlWrapper.connect()
  }

  @AfterEach
  fun afterEach() {
    dropTableIfExists(testTableName)
  }

  @AfterAll
  override fun afterAll() {
    sqlWrapper.disconnect()
    super.afterAll()
  }

  @Test
  fun truncateSchema() {
    assertTableExists(testTableName, exists = false)
    createTable(testTableName)
    assertTableExists(testTableName, exists = true)
    assertTableHasContents(testTableName, hasContents = false)
    insertTableContents(testTableName)
    assertTableHasContents(testTableName, hasContents = true)

    module.truncateSchema()

    assertTableExists(testTableName, exists = true)
    assertTableHasContents(testTableName, hasContents = false)
  }

  private fun createTable(tableName: String) {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("CREATE TABLE $tableName (col TEXT)")
          .execute()
    }
  }

  private fun insertTableContents(tableName: String) {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("INSERT INTO $tableName (col) VALUES ('contents')")
          .execute()
    }
  }

  private fun dropTableIfExists(tableName: String) {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("DROP TABLE IF EXISTS $tableName")
          .execute()
    }
  }

  private fun assertTableExists(tableName: String, exists: Boolean) {
    val tableNames = jdbi.withHandle<Set<String>, Exception> { handle ->
      handle.createQuery("SELECT tablename FROM pg_tables WHERE schemaname = '$testSchemaName'")
          .mapTo(String::class.java)
          .toSet()
    }
    assertEquals(exists, tableName in tableNames)
  }

  private fun assertTableHasContents(tableName: String, hasContents: Boolean) {
    val tableContents = jdbi.withHandle<List<String>, Exception> { handle ->
      handle.createQuery("SELECT col FROM $tableName")
          .mapTo(String::class.java)
          .toList()
    }
    assertEquals(hasContents, tableContents.isNotEmpty())
  }
}
