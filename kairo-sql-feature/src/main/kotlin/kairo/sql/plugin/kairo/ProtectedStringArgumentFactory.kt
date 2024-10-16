package kairo.sql.plugin.kairo

import java.sql.Types
import kairo.protectedString.ProtectedString
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringArgumentFactory : AbstractArgumentFactory<ProtectedString>(Types.VARCHAR) {
  override fun build(value: ProtectedString, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      statement.setString(position, value.value)
    }
}
