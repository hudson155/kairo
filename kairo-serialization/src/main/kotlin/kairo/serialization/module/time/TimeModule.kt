package kairo.serialization.module.time

import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module.
 *
 * The implementations in com.fasterxml.jackson.datatype:jackson-datatype-jsr310
 * are very generous in what formats they accept,
 * and we want to be stricter.
 */
internal class TimeModule : SimpleModule() {
  init {
    configureInstant()
    configureLocalDate()
    configureYearMonth()
    configureZoneId()
  }

  private fun configureInstant() {
    addSerializer(Instant::class.javaObjectType, InstantSerializer())
    addKeySerializer(Instant::class.javaObjectType, InstantSerializer.Key())
    addDeserializer(Instant::class.javaObjectType, InstantDeserializer())
    addKeyDeserializer(Instant::class.javaObjectType, InstantDeserializer.Key())
  }

  private fun configureLocalDate() {
    addSerializer(LocalDate::class.javaObjectType, LocalDateSerializer())
    addKeySerializer(LocalDate::class.javaObjectType, LocalDateSerializer.Key())
    addDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer())
    addKeyDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer.Key())
  }

  private fun configureYearMonth() {
    addSerializer(YearMonth::class.javaObjectType, YearMonthSerializer())
    addKeySerializer(YearMonth::class.javaObjectType, YearMonthSerializer.Key())
    addDeserializer(YearMonth::class.javaObjectType, YearMonthDeserializer())
    addKeyDeserializer(YearMonth::class.javaObjectType, YearMonthDeserializer.Key())
  }

  private fun configureZoneId() {
    addSerializer(ZoneId::class.javaObjectType, ZoneIdSerializer())
    addKeySerializer(ZoneId::class.javaObjectType, ZoneIdSerializer.Key())
    addDeserializer(ZoneId::class.javaObjectType, ZoneIdDeserializer())
    addKeyDeserializer(ZoneId::class.javaObjectType, ZoneIdDeserializer.Key())
  }
}
