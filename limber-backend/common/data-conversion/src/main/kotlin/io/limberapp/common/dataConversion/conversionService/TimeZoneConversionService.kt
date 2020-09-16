package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService
import io.limberapp.common.types.TimeZone
import java.time.ZoneId

object TimeZoneConversionService : DataConversionService<TimeZone> {
  override val kClass = TimeZone::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): TimeZone = TimeZone(ZoneId.of(value))

  override fun toString(value: TimeZone) = value.toString()
}
