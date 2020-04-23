package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.jdbc.PgArray
import java.sql.PreparedStatement

fun Table.stringList(name: String): Column<List<String>> =
    registerColumn(name, StringListColumnType())

class StringListColumnType : ColumnType() {

    override fun sqlType() = "TEXT[]"

    override fun nonNullValueToString(value: Any): String {
        if (value !is String) unexpectedValue(value)
        return value
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PgArray) unexpectedValue(value)
        val array = value.array
        if (array !is Array<*>) unexpectedValue(array)
        return array.map { it.toString() }
    }

    override fun setParameter(stmt: PreparedStatement, index: Int, value: Any?) {
        value ?: return super.setParameter(stmt, index, value)
        if (value !is List<*>) unexpectedValue(value)
        stmt.setArray(index, stmt.connection.createArrayOf("text", value.toTypedArray()))
    }
}
