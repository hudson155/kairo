package kairo.serialization.module.time

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kairo.serialization.ObjectMapperFactoryBuilder

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
    configureZoneId()
  }

  private fun configureInstant() {
    addSerializer(Instant::class.javaObjectType, InstantSerializer())
    addDeserializer(Instant::class.javaObjectType, InstantDeserializer())
    addKeyDeserializer(Instant::class.javaObjectType, InstantDeserializer.Key())
  }

  private fun configureLocalDate() {
    addSerializer(LocalDate::class.javaObjectType, LocalDateSerializer())
    addDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer())
    addKeyDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer.Key())
  }

  private fun configureZoneId() {
    addSerializer(ZoneId::class.javaObjectType, ZoneIdSerializer())
    addKeySerializer(ZoneId::class.javaObjectType, ZoneIdSerializer.Key())
    addDeserializer(ZoneId::class.javaObjectType, ZoneIdDeserializer())
    addKeyDeserializer(ZoneId::class.javaObjectType, ZoneIdDeserializer.Key())
  }
}

internal fun ObjectMapperFactoryBuilder.configureTime() {
  addModule(TimeModule())

  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
  configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
}
