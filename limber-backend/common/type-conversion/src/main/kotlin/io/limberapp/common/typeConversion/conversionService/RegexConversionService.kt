package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.typeConversion.TypeConversionService
import kotlin.reflect.KClass

object RegexConversionService : TypeConversionService<Regex> {
  override val kClass: KClass<Regex> = Regex::class

  // It's impossible to validate Regex statically.
  override fun isValid(value: String): Boolean = true

  override fun parseString(value: String): Regex = Regex(value)

  override fun writeString(value: Regex): String = value.pattern
}
