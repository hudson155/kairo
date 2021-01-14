package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.typeConversion.TypeConversionService

object RegexConversionService : TypeConversionService<Regex> {
  override val kClass = Regex::class

  override fun isValid(value: String) = true // It's impossible to validate Regex statically.

  override fun parseString(value: String) = Regex(value)

  override fun writeString(value: Regex) = value.pattern
}
