package kairo.sqlTesting

import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.getNamedInstance
import kairo.dependencyInjection.getNamedInstanceOptional
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
  private val config: KairoSqlConfig,
  private val schemas: List<String>? = null,
) : KairoSqlFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEach(injector: Injector) {
    injector.getNamedInstance<Sql>(config.name).transaction { handle ->
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
    val migrationConfig = injector.getNamedInstanceOptional<KairoSqlMigrationConfig>(config.name)
      ?: return emptyList()
    return buildList {
      add(migrationConfig.defaultSchema)
      addAll(migrationConfig.schemas)
    }
  }

  private fun filterTables(injector: Injector, tables: List<String>): List<String> {
    val migrationConfig = injector.getNamedInstanceOptional<KairoSqlMigrationConfig>(config.name)
      ?: return tables
    return tables.filter { table ->
      // Don't truncate the migration table, or we'll have to run migrations again.
      table != "${migrationConfig.defaultSchema}.${migrationConfig.tableName}"
    }
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
