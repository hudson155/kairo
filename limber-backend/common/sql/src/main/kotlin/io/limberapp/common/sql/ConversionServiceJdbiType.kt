package io.limberapp.common.sql

import io.limberapp.common.typeConversion.TypeConversionService
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.Types
import kotlin.reflect.KClass

internal abstract class ConversionServiceJdbiType<T : Any>(override val kClass: KClass<T>) : JdbiType<T>() {
  internal abstract class ConversionServiceColumnMapper<T : Any>(
      private val conversionService: TypeConversionService<T>,
  ) : ColumnMapper<T?> {
    final override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): T? =
        r.getString(columnNumber)?.let { conversionService.fromString(it) }
  }

  internal abstract class ConversionServiceArgumentFactory<T : Any>(
      private val conversionService: TypeConversionService<T>,
  ) : AbstractArgumentFactory<T>(Types.VARCHAR) {
    final override fun build(value: T, config: ConfigRegistry): Argument = Argument { position, statement, _ ->
      statement.setString(position, conversionService.toString(value))
    }
  }
}
