package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Table.localDateTime(name: String): Column<LocalDateTime> =
    registerColumn(name, LocalDateTimeColumnType())

class LocalDateTimeColumnType : ColumnType() {

    private val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSSSSS").withLocale(Locale.ROOT)

    override fun sqlType() = "TIMESTAMP"

    override fun nonNullValueToString(value: Any): String {
        return if (value !is LocalDateTime) unexpectedValue(value)
        else value.format(formatter)
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is Timestamp) unexpectedValue(value)
        return value.toLocalDateTime()
    }
}
