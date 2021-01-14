package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.logging.LoggerFactory
import io.limberapp.common.typeConversion.TypeConversionService
import java.time.ZoneId

object TimeZoneConversionService : TypeConversionService<ZoneId> {
  private val logger = LoggerFactory.getLogger(TimeZoneConversionService::class)

  override val kClass = ZoneId::class

  override fun isValid(value: String) = true // It's not practical to validate time zones statically.

  override fun parseString(value: String): ZoneId {
    val zoneId = ZoneId.of(value)
    logger.debug("Converted $value to $zoneId.")
    val normalizedZoneId = zoneId.normalized()
    logger.debug("Normalized $zoneId to $normalizedZoneId.")
    return normalizedZoneId
  }

  override fun writeString(value: ZoneId): String {
    val normalizedValue = value.normalized()
    logger.debug("Normalized $value to $normalizedValue.")
    return normalizedValue.toString()
  }
}
