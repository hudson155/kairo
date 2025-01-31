package kairo.sql.plugin.kairo

import java.sql.ResultSet
import kairo.hugeString.HugeString
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class HugeStringColumnMapper : ColumnMapper<HugeString> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): HugeString? {
    val string = r.getString(columnNumber) ?: return null
    return HugeString(string)
  }
}
