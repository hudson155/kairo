package io.limberapp.typeConversion.conversionService

import io.limberapp.typeConversion.TypeConversionService
import java.time.ZoneId

object TimeZoneConversionService : TypeConversionService<ZoneId> {
  override val kClass = ZoneId::class

  override fun isValid(value: String) = true // It's not practical to validate time zones statically.

  override fun fromString(value: String): ZoneId = ZoneId.of(value)

  override fun toString(value: ZoneId) = value.toString()
}
