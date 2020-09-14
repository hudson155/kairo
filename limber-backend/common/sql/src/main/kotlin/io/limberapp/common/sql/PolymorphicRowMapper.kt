package io.limberapp.common.sql

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

/**
 * Maps abstract classes or interfaces to concrete classes when reading from the database. This is useful when a single
 * table is used to store information from multiple classes with a common base class. In this case, it should be used in
 * conjunction with the [bindNullForMissingArguments] method (for writing).
 *
 * [typeName] is the name of the column that indicates which concrete class should be used.
 *
 * [getClass] should return the appropriate concrete class for the given type string.
 */
abstract class PolymorphicRowMapper<T : Any>(private val typeName: String) : RowMapper<T> {
  final override fun map(rs: ResultSet, ctx: StatementContext): T {
    val kClass = getClass(rs.getString(typeName))
    return ctx.findRowMapperFor(kClass).get().map(rs, ctx)
  }

  abstract fun getClass(type: String): Class<out T>
}
