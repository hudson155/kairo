package com.piperframework.store

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.transaction

abstract class SqlStore(private val database: Database) {

    protected fun <T> transaction(function: Transaction.() -> T) = transaction(database) { function() }

    fun <E : Any> Table.batchInsertIndexed(
        data: Iterable<E>,
        ignore: Boolean = false,
        body: BatchInsertStatement.(Int, E) -> Unit
    ): List<ResultRow> {
        var i = 0
        return batchInsert(data, ignore) {
            body(i, it)
            i++
        }
    }

    fun Table.deleteAtMostOneWhere(op: SqlExpressionBuilder.() -> Op<Boolean>) = deleteWhere(op = op).ifGt(1, ::badSql)

    protected inline fun Int.ifGt(int: Int, function: () -> Nothing): Int = if (this > int) function() else this

    protected inline fun Int.ifEq(int: Int, function: () -> Nothing): Int = if (this == int) function() else this

    protected fun badSql(): Nothing = error("An SQL statement invariant failed. The transaction has been aborted.")
}
