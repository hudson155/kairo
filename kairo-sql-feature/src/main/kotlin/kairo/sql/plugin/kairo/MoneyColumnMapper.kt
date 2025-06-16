package kairo.sql.plugin.kairo

import com.fasterxml.jackson.module.kotlin.readValue
import java.sql.ResultSet
import kairo.sql.sqlReadMapper
import org.javamoney.moneta.Money
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class MoneyColumnMapper : ColumnMapper<Money> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): Money? {
    val string = r.getString(columnNumber) ?: return null
    return sqlReadMapper.readValue(string)
  }
}
