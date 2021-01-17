package io.limberapp.common.typeConversion.typeConverter

import io.limberapp.common.typeConversion.TypeConverter
import kotlin.reflect.KClass

object RegexTypeConverter : TypeConverter<Regex> {
  override val kClass: KClass<Regex> = Regex::class

  // It's impossible to validate Regex statically.
  override fun isValid(value: String): Boolean? = null

  override fun parseString(value: String): Regex = Regex(value)

  override fun writeString(value: Regex): String = value.pattern
}
