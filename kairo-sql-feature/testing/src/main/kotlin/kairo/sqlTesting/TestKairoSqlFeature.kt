package kairo.sqlTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.sql.KairoSqlConfig
import kairo.sql.KairoSqlFeature
import kairo.sql.Sql

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
          select tablename
          from pg_tables
          where schemaname in ${schemaNames.joinToString { "'$it'" }}
        """.trimIndent(),
      ).mapToMap().toList()
      if (tables.isNotEmpty()) {
        handle.createUpdate(
          """
            truncate ${tables.joinToString { "${it["schemaname"]}.${it["tablename"]}" }}
          """.trimIndent(),
        ).execute()
      }
    }
  }
}
