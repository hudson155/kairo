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

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Extends [KairoSqlFeature] for testing by automatically truncating all tables before each test.
 */
public open class TestKairoSqlFeature(
  config: KairoSqlConfig,
  private val schemaNames: List<String>,
) : KairoSqlFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEachTest(injector: Injector) {
    injector.getInstance<Sql>().sql { handle ->
      val tables = handle.createQuery(
        """
          select concat_ws('.', schemaname, tablename)
          from pg_tables
          where schemaname in ${schemaNames.joinToString { "'$it'" }}
        """.trimIndent(),
      ).mapTo<String>().toList()

      if (tables.isEmpty()) {
        logger.debug { "No tables to truncate." }
        return@sql
      }

      logger.debug { "Truncating tables: $tables." }
      handle.createUpdate("truncate ${tables.joinToString()}").execute()
    }
  }
}
