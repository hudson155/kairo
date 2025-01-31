package kairo.sql.plugin.kairo

import java.sql.Types
import kairo.doNotLogString.DoNotLogString
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry

public class DoNotLogStringArgumentFactory : AbstractArgumentFactory<DoNotLogString>(Types.VARCHAR) {
  override fun build(value: DoNotLogString, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      statement.setString(position, value.value)
    }
}
