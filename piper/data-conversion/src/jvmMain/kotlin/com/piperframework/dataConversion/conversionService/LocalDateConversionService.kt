package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

actual object LocalDateConversionService : DataConversionService<LocalDate> {
  override val kClass = LocalDate::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): LocalDate =
    LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)

  override fun toString(value: LocalDate): String =
    value.format(DateTimeFormatter.ISO_LOCAL_DATE)
}
