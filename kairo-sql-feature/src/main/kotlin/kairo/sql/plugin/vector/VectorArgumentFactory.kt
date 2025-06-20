package kairo.sql.plugin.vector

import java.sql.Types
import java.util.Optional
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.argument.QualifiedArgumentFactory
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.qualifier.QualifiedType
import org.postgresql.util.PGobject

internal class VectorArgumentFactory : QualifiedArgumentFactory {
  override fun build(qualifiedType: QualifiedType<*>, value: Any?, config: ConfigRegistry): Optional<Argument> {
    if (!qualifiedType.qualifiers.any { it.annotationClass == Vector::class }) return Optional.empty()
    @Suppress("UNCHECKED_CAST")
    value as List<Float>?
    val argument = Argument { position, statement, _ ->
      if (value == null) {
        statement.setNull(position, Types.OTHER)
        return@Argument
      }
      val pgObj = PGobject().apply {
        this.type = "vector"
        this.value = value.joinToString(prefix = "[", postfix = "]", separator = ",")
      }
      statement.setObject(position, pgObj)
    }
    return Optional.of(argument)
  }
}
