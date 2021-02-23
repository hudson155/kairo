package io.limberapp.module

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import io.limberapp.typeConversion.TypeConverter
import io.limberapp.typeConversion.typeConverter.LimberPermissionsTypeConverter
import io.limberapp.typeConversion.typeConverter.TimeZoneTypeConverter
import io.limberapp.typeConversion.typeConverter.UuidTypeConverter

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
