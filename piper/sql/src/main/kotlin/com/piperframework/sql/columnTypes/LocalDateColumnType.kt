package com.piperframework.sql.columnTypes

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Table.localDate(name: String): Column<LocalDate> = registerColumn(name, LocalDateColumnType())

class LocalDateColumnType : ColumnType() {

    private val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd").withLocale(Locale.ROOT)

    override fun sqlType() = "DATE"

    override fun nonNullValueToString(value: Any): String {
        return if (value !is LocalDate) unexpectedValue(value)
        else value.format(formatter)
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is Date) unexpectedValue(value)
        return value.toLocalDate()
    }
}
