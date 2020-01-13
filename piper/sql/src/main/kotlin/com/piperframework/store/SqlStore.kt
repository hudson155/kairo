package com.piperframework.store

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

abstract class SqlStore(private val database: Database) {

    protected fun <T> transaction(function: Transaction.() -> T) = transaction(database) { function() }

    protected inline fun Int.ifGt(int: Int, function: () -> Nothing): Int = if (this > int) function() else this

    protected inline fun Int.ifEq(int: Int, function: () -> Nothing): Int = if (this == int) function() else this

    protected fun badSql(): Nothing = error("An SQL statement invariant failed. The transaction has been aborted.")
}
