package kairo.sql.plugin.kairo

import java.sql.Types
import kotlin.uuid.Uuid
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.postgresql.util.PGobject

internal class UuidArgumentFactory : AbstractArgumentFactory<Uuid>(Types.OTHER) {
  override fun build(value: Uuid, config: ConfigRegistry): Argument =
    Argument { position, statement, _ ->
      val pgObject = PGobject().apply {
        this.type = "uuid"
        this.value = value.toString()
      }
      statement.setObject(position, pgObject)
    }
}
