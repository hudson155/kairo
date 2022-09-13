package limber.sql

import limber.config.SqlConfig
import limber.feature.TestFeature
import java.sql.Connection

public class TestSqlFeature(
  config: SqlConfig,
  private val schemaNames: Set<String>,
) : SqlFeature(config), TestFeature {
  public constructor(config: SqlConfig, schemaName: String) : this(config, setOf(schemaName))

  override fun afterEach() {
    checkNotNull(dataSource).connection.use { connection ->
      connection.truncateAllTables()
    }
  }

  @Suppress("SqlNoDataSourceInspection", "SqlResolve")
  private fun Connection.truncateAllTables() {
    val select = """
      select schemaname, tablename
      from pg_tables
      where schemaname in (${schemaNames.joinToString { "'$it'" }})
    """.trimIndent()
    val resultSet = createStatement().executeQuery(select)
    val tables = buildList<Pair<String, String>> {
      while (resultSet.next()) {
        add(Pair(resultSet.getString("schemaname"), resultSet.getString("tablename")))
      }
    }
    val tableNames = tables.joinToString { "${it.first}.${it.second}" }
    val drop = "truncate $tableNames"
    createStatement().execute(drop)
  }
}
