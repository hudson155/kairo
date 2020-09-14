package io.limberapp.common.sql

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.mapper.ColumnMapper
import java.lang.reflect.Type
import kotlin.reflect.KClass

abstract class JdbiType<T : Any> {
  protected abstract val kClass: KClass<T>
  val type get() = kClass.java as Type
  abstract val columnMapper: ColumnMapper<T?>
  abstract val argumentFactory: AbstractArgumentFactory<T>
}

fun <T : Any> Jdbi.registerJdbiType(jdbiType: JdbiType<T>): Jdbi {
  registerColumnMapper(jdbiType.type, jdbiType.columnMapper)
  registerArgument(jdbiType.argumentFactory)
  return this
}
