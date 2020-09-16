package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService

object LocalDateConversionService : DataConversionService<String> {
  override val kClass = String::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String) = value

  override fun toString(value: String) = value
}
