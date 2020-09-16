package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeConversionService : DataConversionService<LocalDateTime> {
  override val kClass = LocalDateTime::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): LocalDateTime =
    LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

  override fun toString(value: LocalDateTime): String =
    value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
