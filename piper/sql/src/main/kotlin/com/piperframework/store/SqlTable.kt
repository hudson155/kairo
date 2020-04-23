package com.piperframework.store

import com.piperframework.sql.columnTypes.localDateTime
import org.jetbrains.exposed.dao.IdTable

/**
 * Represents a table in the SQL database.
 */
abstract class SqlTable(schema: String, tableName: String) : IdTable<Long>() {

    final override val tableName = "$schema.$tableName"

    final override val id = long("id").entityId()
    val createdDate = localDateTime("created_date")
}
