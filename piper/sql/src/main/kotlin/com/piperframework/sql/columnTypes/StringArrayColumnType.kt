package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.jdbc.PgArray

fun Table.stringArray(name: String): Column<Array<String>> =
    registerColumn(name, StringArrayColumnType())

class StringArrayColumnType : ColumnType() {
    override fun sqlType() = "TEXT[]"

    override fun valueToString(value: Any?): String {
        if (value is Array<*>) return value.joinToString(separator = ",") { valueToString(it) }
        return super.valueToString(value)
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PgArray) unexpectedValue(value)
        return value.array
    }
}
