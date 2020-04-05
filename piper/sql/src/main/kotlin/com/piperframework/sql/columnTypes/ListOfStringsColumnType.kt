package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.jdbc.PgArray
import java.sql.PreparedStatement

fun Table.listOfStrings(name: String): Column<List<String>> = registerColumn(
    name,
    ListOfStringsColumnType()
)

class ListOfStringsColumnType : ColumnType() {

    override fun sqlType() = "TEXT[]"

    override fun valueFromDB(value: Any): List<String> = when (value) {
        is Iterable<*> -> value.map{it.toString()}
        is PgArray -> {
            val array = value.array
            if (array is Array<*>) array.map{ it.toString() }
            else unexpectedValue(array)
        }
        else -> unexpectedValue(value)
    }

    override fun valueToString(value: Any?): String = when (value) {
        null -> {
            if (!nullable) error("NULL in non-nullable column")
            "NULL"
        }
        is Iterable<*> -> "'{${value.joinToString()}}'"
        else -> nonNullValueToString(value)
    }

    override fun setParameter(stmt: PreparedStatement, index: Int, value: Any?) {
        if (value is List<*>) {
            stmt.setArray(index, stmt.connection.createArrayOf("text", value.toTypedArray()))
        } else {
            super.setParameter(stmt, index, value)
        }
    }

    private fun unexpectedValue(value: Any): Nothing =
        error("Unexpected value: $value of ${value::class.qualifiedName}")
}
