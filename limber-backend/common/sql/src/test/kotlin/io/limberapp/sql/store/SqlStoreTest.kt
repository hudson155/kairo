package io.limberapp.sql.store

import io.limberapp.config.SqlDatabaseConfig
import io.limberapp.sql.createDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.postgresql.util.ServerErrorMessage
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class SqlStoreTest : SqlStore(
    jdbi = Jdbi.create(SqlDatabaseConfig(
        jdbcUrl = "jdbc:postgresql://localhost/limber",
        defaultSchema = "sqltest",
        username = System.getenv("LIMBER_TEST_POSTGRES_USERNAME") ?: "postgres",
        password = System.getenv("LIMBER_TEST_POSTGRES_PASSWORD"),
    ).createDataSource())
) {
  @BeforeEach
  fun beforeEach() {
    withHandle { handle ->
      val tableNames = listOf("sql_store_test", "sql_store_test_1", "sql_store_test_2")
      handle.createUpdate("DROP TABLE IF EXISTS ${tableNames.joinToString()}")
          .execute()
    }
  }

  @Test
  fun sqlResource() {
    assertEquals("SELECT 1 FROM foo\n", sqlResource("store/test/foo.sql"))
    assertFails { sqlResource("store/test/baz.sql") }
  }

  @Test
  fun `serverErrorMessage - isNotNullConstraintViolation`() {
    inTransaction { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test (col TEXT NOT NULL)")
          .execute()
      try {
        handle.createUpdate("INSERT INTO sql_store_test (col) VALUES (NULL)")
            .execute()
      } catch (e: UnableToExecuteStatementException) {
        assertServerErrorMessage(e.serverErrorMessage) {
          it.isNotNullConstraintViolation("col")
        }
      }
    }
  }

  @Test
  fun `serverErrorMessage - isForeignKeyViolation`() {
    inTransaction { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test_1 (col TEXT UNIQUE)")
          .execute()
      handle.createUpdate("CREATE TABLE sql_store_test_2 (col TEXT REFERENCES sql_store_test_1 (col))")
          .execute()
      try {
        handle.createUpdate("INSERT INTO sql_store_test_2 (col) VALUES ('val1')")
            .execute()
      } catch (e: UnableToExecuteStatementException) {
        assertServerErrorMessage(e.serverErrorMessage) {
          it.isForeignKeyViolation("sql_store_test_2_col_fkey")
        }
      }
    }
  }

  @Test
  fun `serverErrorMessage - isUniqueConstraintViolation`() {
    inTransaction { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test (col TEXT UNIQUE)")
          .execute()
      handle.createUpdate("INSERT INTO sql_store_test (col) VALUES ('val1')")
          .execute()
      try {
        handle.createUpdate("INSERT INTO sql_store_test (col) VALUES ('val1')")
            .execute()
      } catch (e: UnableToExecuteStatementException) {

        assertServerErrorMessage(e.serverErrorMessage) {
          it.isUniqueConstraintViolation("sql_store_test_col_key")
        }
      }
    }
  }

  @Test
  fun withHandle() {
    withHandle { handle ->
      handle.createQuery("SELECT 'val1'")
          .mapTo(String::class.java)
          .one()
    }.let { assertEquals("val1", it) }
  }

  @Test
  fun `inTransaction - commit`() {
    withHandle { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test (col TEXT)")
          .execute()
    }
    inTransaction { handle ->
      handle.createQuery("INSERT INTO sql_store_test (col) VALUES ('val1') RETURNING col")
          .mapTo(String::class.java)
          .one()
    }.let { assertEquals("val1", it) }
    withHandle { handle ->
      handle.createQuery("SELECT col FROM sql_store_test")
          .mapTo(String::class.java)
          .one()
    }.let { assertEquals("val1", it) }
  }

  @Test
  fun `inTransaction - rollback`() {
    withHandle { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test (col TEXT)")
          .execute()
    }
    try {
      inTransaction { handle ->
        handle.createQuery("INSERT INTO sql_store_test (col) VALUES ('val1') RETURNING col")
            .mapTo(String::class.java)
            .one()
        throw RuntimeException()
      }
    } catch (_: RuntimeException) {
    }
    withHandle { handle ->
      handle.createQuery("SELECT col FROM sql_store_test")
          .mapTo(String::class.java)
          .findOne().orElse(null)
    }.let { assertNull(it) }
  }

  @Test
  fun update() {
    withHandle { handle ->
      handle.createUpdate("CREATE TABLE sql_store_test (col TEXT)")
          .execute()
    }
    withHandle { handle ->
      handle.createUpdate("UPDATE sql_store_test SET col = 'new val'")
          .update()
          .let { assertNull(it) }
    }
    withHandle { handle ->
      handle.createUpdate("INSERT INTO sql_store_test (col) VALUES ('val1')")
          .execute()
      handle.createUpdate("UPDATE sql_store_test SET col = 'new val'")
          .update()
          .let { assertEquals(Unit, it) }
    }
    withHandle { handle ->
      handle.createUpdate("INSERT INTO sql_store_test (col) VALUES ('val1')")
          .execute()
      assertFailsWith<IllegalStateException> {
        handle.createUpdate("UPDATE sql_store_test SET col = 'new val'")
            .update()
      }
    }
  }

  private fun assertServerErrorMessage(
      serverErrorMessage: ServerErrorMessage?,
      function: (ServerErrorMessage) -> Boolean,
  ) {
    assertTrue(function(assertNotNull(serverErrorMessage)), serverErrorMessage.toString())
  }
}

