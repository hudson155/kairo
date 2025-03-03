package kairo.sql.plugin.kairo

import java.sql.Types
import java.time.YearMonth
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry

public class YearMonthArgumentFactory : AbstractArgumentFactory<YearMonth>(Types.VARCHAR) {
  override fun build(value: YearMonth, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      statement.setString(position, value.toString())
    }
}
