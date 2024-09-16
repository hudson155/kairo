package kairo.sql

import java.sql.Types
import kairo.id.KairoId
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry

internal class KairoIdArgumentFactory : AbstractArgumentFactory<KairoId>(Types.VARCHAR) {
  override fun build(value: KairoId, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      statement.setString(position, value.toString())
    }
}
