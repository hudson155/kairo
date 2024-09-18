package kairo.sqlTesting

import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.getInstanceOptional
import kairo.featureTesting.TestFeature
import kairo.sql.KairoSqlConfig
import kairo.sql.KairoSqlFeature
import kairo.sql.Sql
import kairo.sqlMigration.KairoSqlMigrationConfig
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Extends [KairoSqlFeature] for testing by automatically truncating all tables before each test.
 * Tables in [schemas] will be truncated.
 * If [schemas] is null, [KairoSqlMigrationConfig.schemas] will be used instead.
 */
public open class TestKairoSqlFeature(
  config: KairoSqlConfig,
  private val schemas: List<String>? = null,
) : KairoSqlFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEach(injector: Injector) {
    injector.getInstance<Sql>().sql { handle ->
      val tables = handle.getTables(injector)
      handle.truncateTables(tables)
    }
  }

  private fun Handle.getTables(injector: Injector): List<String> {
    val schemas = getSchemas(injector)
    if (schemas.isEmpty()) return emptyList()
    val tables = createQuery(
      """
        select concat_ws('.', schemaname, tablename)
        from pg_tables
        where schemaname in (${schemas.joinToString { "'$it'" }})
      """.trimIndent(),
    ).mapTo<String>().toList()
    return filterTables(injector, tables)
  }

  private fun getSchemas(injector: Injector): List<String> {
    if (schemas != null) return schemas
    val migrationConfig = injector.getInstanceOptional<KairoSqlMigrationConfig>() ?: return emptyList()
    return buildList {
      add(migrationConfig.defaultSchema)
      addAll(migrationConfig.schemas)
    }
  }

  private fun filterTables(injector: Injector, tables: List<String>): List<String> {
    val migrationConfig = injector.getInstanceOptional<KairoSqlMigrationConfig>() ?: return tables
    return tables.filter { it != "${migrationConfig.defaultSchema}.${migrationConfig.tableName}" }
  }

  private fun Handle.truncateTables(tables: List<String>) {
    if (tables.isEmpty()) {
      logger.debug { "No tables to truncate." }
      return
    }
    logger.debug { "Truncating tables: $tables." }
    createUpdate("truncate ${tables.joinToString()}").execute()
  }
}
