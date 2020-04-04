package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.jdbc.PgArray

fun Table.list(name: String): Column<List<String>> = registerColumn(
    name,
    SetColumnType()
)

class SetColumnType : ColumnType() {

    override fun sqlType() = "TEXT[]"

    override fun valueFromDB(value: Any): Any = when (value) {
        is Iterable<*> -> value
        is PgArray -> value.array
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

    private fun unexpectedValue(value: Any): Nothing =
        error("Unexpected value: $value of ${value::class.qualifiedName}")
}
