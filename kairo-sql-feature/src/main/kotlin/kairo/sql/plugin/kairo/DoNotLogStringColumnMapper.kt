package kairo.sql.plugin.kairo

import java.sql.ResultSet
import kairo.doNotLogString.DoNotLogString
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class DoNotLogStringColumnMapper : ColumnMapper<DoNotLogString> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): DoNotLogString? {
    val string = r.getString(columnNumber) ?: return null
    return DoNotLogString(string)
  }
}
