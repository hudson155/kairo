package kairo.sql.plugin.kairo

import java.sql.ResultSet
import kotlin.uuid.Uuid
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class UuidColumnMapper : ColumnMapper<Uuid> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): Uuid? {
    val string = r.getString(columnNumber) ?: return null
    return Uuid.parse(string)
  }
}
