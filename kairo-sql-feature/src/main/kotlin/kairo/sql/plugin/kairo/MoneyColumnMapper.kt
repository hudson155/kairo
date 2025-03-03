package kairo.sql.plugin.kairo

import io.github.projectmapk.jackson.module.kogera.readValue
import java.sql.ResultSet
import kairo.sql.sqlMapper
import org.javamoney.moneta.Money
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class MoneyColumnMapper : ColumnMapper<Money> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): Money? {
    val string = r.getString(columnNumber) ?: return null
    return sqlMapper.readValue(string)
  }
}
