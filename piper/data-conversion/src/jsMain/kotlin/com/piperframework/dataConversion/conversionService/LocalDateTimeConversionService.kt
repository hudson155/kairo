package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import kotlin.js.Date

actual object LocalDateTimeConversionService : DataConversionService<Date> {
  override val kClass = Date::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String) = Date("${value}Z")

  override fun toString(value: Date) = with(value.toISOString()) {
    check(last() == 'Z')
    return@with dropLast(1)
  }
}
