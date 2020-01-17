package com.piperframework.sql.columnTypes.localDateTime

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Table.localdatetime(name: String): Column<LocalDateTime> = registerColumn(name, LocalDateTimeColumnType())

class LocalDateTimeColumnType : ColumnType() {

    private val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSSSSS").withLocale(Locale.ROOT)

    override fun sqlType() = TransactionManager.current().db.dialect.dataTypeProvider.dateTimeType()

    override fun nonNullValueToString(value: Any): String {
        return if (value !is LocalDateTime) unexpectedValue(value)
        else value.format(formatter)
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is Timestamp) unexpectedValue(value)
        return value.toLocalDateTime()
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value !is LocalDateTime) unexpectedValue(value)
        return Timestamp.valueOf(value)
    }

    private fun unexpectedValue(value: Any): Nothing =
        error("Unexpected value: $value of ${value::class.qualifiedName}")
}
