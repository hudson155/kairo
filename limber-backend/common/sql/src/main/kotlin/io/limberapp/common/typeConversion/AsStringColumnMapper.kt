package io.limberapp.common.typeConversion

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

internal class AsStringColumnMapper(private val converter: TypeConverter<*>) : ColumnMapper<Any?> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): Any? =
      r.getString(columnNumber)?.let { converter.parseString(it) }
}
