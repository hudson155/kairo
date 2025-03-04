package kairo.sql.plugin.kairo

import java.sql.Types
import kairo.serialization.util.kairoWrite
import kairo.sql.sqlMapper
import org.javamoney.moneta.Money
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.postgresql.util.PGobject

public class MoneyArgumentFactory : AbstractArgumentFactory<Money>(Types.OTHER) {
  override fun build(value: Money, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      val pgObject = PGobject().apply {
        type = "jsonb"
        setValue(sqlMapper.kairoWrite(value))
      }
      statement.setObject(position, pgObject)
    }
}
