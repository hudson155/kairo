package io.limberapp.typeConversion

import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.argument.ArgumentFactory
import org.jdbi.v3.core.argument.NullArgument
import org.jdbi.v3.core.config.ConfigRegistry
import java.lang.reflect.Type
import java.sql.Types
import java.util.Optional
import java.util.function.Function

@OptIn(TypeConverter.Unsafe::class)
internal class AsStringArgumentFactory(private val converter: TypeConverter<*>) :
    ArgumentFactory.Preparable {
  override fun build(type: Type, value: Any?, config: ConfigRegistry): Optional<Argument> {
    if (!converter.canConvert(type)) return Optional.empty()
    return Optional.of(bind(value))
  }

  override fun prepare(type: Type, config: ConfigRegistry): Optional<Function<Any, Argument>> {
    if (!converter.canConvert(type)) return Optional.empty()
    return Optional.of(Function { bind(it) })
  }

  private fun bind(value: Any?): Argument {
    if (value == null) return NullArgument(Types.VARCHAR)
    return Argument { position, statement, _ ->
      statement.setString(position, converter.writeStringUnsafe(value))
    }
  }

  override fun prePreparedTypes(): Collection<Type> = listOf(converter.kClass.java)
}
