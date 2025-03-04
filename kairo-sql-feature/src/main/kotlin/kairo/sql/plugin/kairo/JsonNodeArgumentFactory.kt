package kairo.sql.plugin.kairo

import com.fasterxml.jackson.databind.JsonNode
import java.sql.Types
import kairo.serialization.util.kairoWrite
import kairo.sql.sqlMapper
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.postgresql.util.PGobject

public class JsonNodeArgumentFactory : AbstractArgumentFactory<JsonNode>(Types.OTHER) {
  override fun build(value: JsonNode, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      val pgObject = PGobject().apply {
        type = "jsonb"
        setValue(sqlMapper.kairoWrite(value))
      }
      statement.setObject(position, pgObject)
    }
}
