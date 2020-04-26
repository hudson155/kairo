package com.piperframework.store

import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage
import java.sql.BatchUpdateException

@Suppress("UnnecessaryAbstractClass")
abstract class SqlStore(private val database: Database) {
    private val resourceCache = ResourceCache()

    /**
     * Fetches the given SQL resource, throwing an exception if it does not exist. The implementation uses a cache that
     * never evicts.
     */
    protected fun sqlResource(name: String): String {
        val classResourcePath = checkNotNull(this::class.qualifiedName).replace('.', '/')
        val resourceName = "/$classResourcePath/$name.sql"
        return resourceCache.get(resourceName)
    }

    protected val UnableToExecuteStatementException.serverErrorMessage: ServerErrorMessage?
        get() {
            val cause = cause
            return when (cause) {
                is BatchUpdateException -> cause.cause as? PSQLException
                is PSQLException -> cause
                else -> null
            }?.serverErrorMessage
        }

    protected fun <T> transaction(function: Transaction.() -> T) = transaction(database) { function() }

    protected data class Operation(val operation: () -> Unit)

    protected data class OperationError(val error: ServerErrorMessage)

    protected fun doOperation(operation: () -> Unit): Operation = Operation(operation)

    protected infix fun Operation.andHandleError(onError: OperationError.() -> Unit) {
        try {
            operation()
        } catch (e: ExposedSQLException) {
            e.cause?.let {
                return@let when (it) {
                    is PSQLException -> it
                    is BatchUpdateException -> it.cause as? PSQLException
                    else -> null
                }
            }?.let { OperationError(it.serverErrorMessage).onError() }
            throw e
        }
    }

    protected fun <E : Any> Table.batchInsertIndexed(
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

    protected fun <T : Table> T.updateExactlyOne(
        where: (SqlExpressionBuilder.() -> Op<Boolean>),
        body: T.(UpdateStatement) -> Unit,
        notFound: () -> Nothing
    ) = update(where = where, body = body).ifGt(1, ::badSql).ifEq(0, notFound)

    protected fun Table.deleteExactlyOne(
        where: SqlExpressionBuilder.() -> Op<Boolean>,
        notFound: () -> Nothing
    ) = deleteWhere(op = where).ifGt(1, ::badSql).ifEq(0, notFound)

    private inline fun Int.ifGt(int: Int, function: () -> Nothing): Int = if (this > int) function() else this

    private inline fun Int.ifEq(int: Int, function: () -> Nothing): Int = if (this == int) function() else this

    protected fun badSql(): Nothing = error("An SQL statement invariant failed. The transaction has been aborted.")
}
