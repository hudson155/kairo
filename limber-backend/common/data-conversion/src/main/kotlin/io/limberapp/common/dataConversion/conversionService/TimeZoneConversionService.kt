package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService
import java.time.ZoneId

object TimeZoneConversionService : DataConversionService<ZoneId> {
  override val kClass = ZoneId::class

  override fun isValid(value: String) = true // Not implemented.

  override fun fromString(value: String): ZoneId = ZoneId.of(value)

  override fun toString(value: ZoneId) = value.toString()
}
