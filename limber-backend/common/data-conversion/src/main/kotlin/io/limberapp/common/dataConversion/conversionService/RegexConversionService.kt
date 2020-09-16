package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService

object RegexConversionService : DataConversionService<Regex> {
  override val kClass = Regex::class

  override fun isValid(value: String) = true // Impossible to validate Regex statically.

  override fun fromString(value: String) = Regex(value)

  override fun toString(value: Regex) = value.pattern
}
