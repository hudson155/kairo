package io.limberapp.backend.module

import io.limberapp.common.config.database.SqlDatabaseConfig
import java.sql.Connection

open class TestSqlModule(config: SqlDatabaseConfig) : LimberSqlModule(config, runMigrations = true) {
  fun dropDatabase() {
    checkNotNull(wrapper.dataSource).connection.use { connection ->
      val customSchemas = listOf("auth", "forms", "orgs", "users")
      connection.truncateAllTables(customSchemas)
    }
  }

  @Suppress("SqlNoDataSourceInspection", "SqlResolve")
  private fun Connection.truncateAllTables(schemas: List<String>) {
    val schemaNames = schemas.joinToString(separator = ", ", transform = { "'$it'" })
    val select = "SELECT schemaname, tablename FROM pg_tables WHERE schemaname IN ($schemaNames)"
    val resultSet = createStatement().executeQuery(select)
    val tables = mutableListOf<Pair<String, String>>()
    while (resultSet.next()) tables.add(Pair(resultSet.getString("schemaname"), resultSet.getString("tablename")))
    val tableNames = tables.joinToString(separator = ", ", transform = { "${it.first}.${it.second}" })
    val drop = "TRUNCATE $tableNames"
    createStatement().execute(drop)
  }
}
