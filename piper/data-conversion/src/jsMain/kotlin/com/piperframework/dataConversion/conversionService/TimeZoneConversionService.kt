package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService

actual object TimeZoneConversionService : DataConversionService<String> {
  override val kClass = String::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): String = value

  override fun toString(value: String) = value
}
