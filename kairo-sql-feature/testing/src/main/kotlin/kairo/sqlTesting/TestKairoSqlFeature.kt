package kairo.sqlTesting

import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.sql.KairoSqlConfig
import kairo.sql.KairoSqlFeature
import kairo.sql.Sql
import kairo.sql.mapTo
import org.jdbi.v3.core.Handle

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Extends [KairoSqlFeature] for testing by automatically truncating all tables before each test.
 * Tables in [schemas] will be truncated.
 */
public open class TestKairoSqlFeature(
  config: KairoSqlConfig,
  private val schemas: List<String>,
) : KairoSqlFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEach(injector: Injector) {
    injector.getInstance<Sql>().sql { handle ->
      val tables = handle.getTables()
      handle.truncateTables(tables)
    }
  }

  private fun Handle.getTables(): List<String> {
    val schemas = getSchemas()
    if (schemas.isEmpty()) return emptyList()
    return createQuery(
      """
        select concat_ws('.', schemaname, tablename)
        from pg_tables
        where schemaname in (${schemas.joinToString { "'$it'" }})
      """.trimIndent(),
    ).mapTo<String>().toList()
  }

  private fun getSchemas(): List<String> =
    schemas

  private fun Handle.truncateTables(tables: List<String>) {
    if (tables.isEmpty()) {
      logger.debug { "No tables to truncate." }
      return
    }
    logger.debug { "Truncating tables: $tables." }
    createUpdate("truncate ${tables.joinToString()}").execute()
  }
}
