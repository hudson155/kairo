package com.piperframework.sql

import com.piperframework.dataConversion.DataConversionService
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
        private val conversionService: DataConversionService<T>
    ) : ColumnMapper<T?> {
        override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): T? =
            r.getString(columnNumber)?.let { conversionService.fromString(it) }
    }

    internal abstract class ConversionServiceArgumentFactory<T : Any>(
        private val conversionService: DataConversionService<T>
    ) : AbstractArgumentFactory<T>(Types.VARCHAR) {
        final override fun build(value: T, config: ConfigRegistry): Argument = Argument { position, statement, _ ->
            statement.setString(position, conversionService.toString(value))
        }
    }
}
