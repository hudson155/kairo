package kairo.sql.plugin.kairo

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import java.sql.ResultSet
import kairo.sql.sqlMapper
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext

public class JsonNodeColumnMapper : ColumnMapper<JsonNode> {
  override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): JsonNode? {
    val string = r.getString(columnNumber) ?: return null
    return sqlMapper.readValue(string)
  }
}
