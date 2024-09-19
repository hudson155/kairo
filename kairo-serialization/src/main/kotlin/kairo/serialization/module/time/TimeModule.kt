package kairo.serialization.module.time

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
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
  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addSerializers(buildSerializers())
    context.addDeserializers(buildDeserializers())
  }

  private fun buildSerializers(): Serializers =
    SimpleSerializers().apply {
      addSerializer(Instant::class.javaObjectType, InstantSerializer())
      addSerializer(LocalDate::class.javaObjectType, LocalDateSerializer())
      addSerializer(ZoneId::class.javaObjectType, ZoneIdSerializer())
    }

  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      addDeserializer(Instant::class.javaObjectType, InstantDeserializer())
      addDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer())
      addDeserializer(ZoneId::class.javaObjectType, ZoneIdDeserializer())
    }
}

internal fun ObjectMapperFactoryBuilder.configureTime() {
  addModule(TimeModule())

  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
  configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
}
