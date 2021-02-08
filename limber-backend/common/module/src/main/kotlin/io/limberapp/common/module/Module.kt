package io.limberapp.common.module

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import io.limberapp.common.typeConversion.TypeConverter
import io.limberapp.common.typeConversion.typeConverter.LimberPermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.TimeZoneTypeConverter
import io.limberapp.common.typeConversion.typeConverter.UuidTypeConverter

private val DEFAULT_TYPE_CONVERTERS: Set<TypeConverter<out Any>> = setOf(
    LimberPermissionsTypeConverter,
    TimeZoneTypeConverter,
    UuidTypeConverter,
)

abstract class Module : AbstractModule() {
  open val typeConverters: Set<TypeConverter<*>> = emptySet()

  override fun configure() {
    bind()
  }

  protected abstract fun bind()

  abstract fun cleanUp()
}

val Set<Module>.typeConverters: Set<TypeConverter<*>>
  get() = DEFAULT_TYPE_CONVERTERS + flatMap { it.typeConverters }

inline fun <reified T : Any> typeLiteral(): TypeLiteral<T> =
    object : TypeLiteral<T>() {}
