package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import com.piperframework.types.TimeZone
import java.time.ZoneId

actual object TimeZoneConversionService : DataConversionService<TimeZone> {
  override val kClass = TimeZone::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): TimeZone = TimeZone(ZoneId.of(value))

  override fun toString(value: TimeZone) = value.zoneId.toString()
}
