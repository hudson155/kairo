package kairo.sql.plugin.kairo

import java.sql.ResultSet
import kairo.id.KairoId
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

internal class KairoIdColumnMapper : ColumnMapper<KairoId> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): KairoId? {
    val string = r.getString(columnNumber) ?: return null
    return KairoId.fromString(string)
  }
}
