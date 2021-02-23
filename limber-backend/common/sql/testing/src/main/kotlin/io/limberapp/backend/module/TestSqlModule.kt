package io.limberapp.backend.module

import io.limberapp.backend.module.sql.SqlModule
import io.limberapp.common.sql.SqlWrapper
import java.sql.Connection

class TestSqlModule(
    sqlWrapper: SqlWrapper,
    private val schemaName: String,
) : SqlModule(sqlWrapper, runMigrations = true) {
  fun truncateSchema() {
    checkNotNull(wrapper.dataSource).connection.use { connection ->
      connection.truncateAllTables(schemaName)
    }
  }

  @Suppress("SqlNoDataSourceInspection", "SqlResolve")
  private fun Connection.truncateAllTables(schemaName: String) {
    val select = "SELECT schemaname, tablename FROM pg_tables WHERE schemaname = '$schemaName'"
    val resultSet = createStatement().executeQuery(select)
    val tables = mutableListOf<Pair<String, String>>()
    while (resultSet.next()) {
      tables.add(Pair(resultSet.getString("schemaname"), resultSet.getString("tablename")))
    }
    val tableNames = tables.joinToString { "${it.first}.${it.second}" }
    val drop = "TRUNCATE $tableNames"
    createStatement().execute(drop)
  }
}
