package kairo.sql.plugin.kairo

import java.sql.ResultSet
import kairo.protectedString.ProtectedString
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

@OptIn(ProtectedString.Access::class)
public class ProtectedStringColumnMapper : ColumnMapper<ProtectedString> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): ProtectedString? {
    val string = r.getString(columnNumber) ?: return null
    return ProtectedString(string)
  }
}
