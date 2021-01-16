package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.typeConversion.TypeConversionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneId
import kotlin.reflect.KClass

object TimeZoneConversionService : TypeConversionService<ZoneId> {
  private val logger: Logger = LoggerFactory.getLogger(TimeZoneConversionService::class.java)

  override val kClass: KClass<ZoneId> = ZoneId::class

  // It's not practical to validate time zones statically.
  override fun isValid(value: String): Boolean = true

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
