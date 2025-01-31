package kairo.sql.plugin.kairo

import java.sql.Types
import kairo.hugeString.HugeString
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry

public class HugeStringArgumentFactory : AbstractArgumentFactory<HugeString>(Types.VARCHAR) {
  override fun build(value: HugeString, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      statement.setString(position, value.value)
    }
}
