package kairo.sql.plugin.kairo

import java.sql.ResultSet
import java.time.YearMonth
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class YearMonthColumnMapper : ColumnMapper<YearMonth> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): YearMonth? {
    val string = r.getString(columnNumber) ?: return null
    return YearMonth.parse(string)
  }
}
