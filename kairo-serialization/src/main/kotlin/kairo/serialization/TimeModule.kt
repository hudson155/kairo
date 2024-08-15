package kairo.serialization

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
import java.time.Instant
import java.time.LocalDate

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module.
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
    }

  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      addDeserializer(Instant::class.javaObjectType, InstantDeserializer())
      addDeserializer(LocalDate::class.javaObjectType, LocalDateDeserializer())
    }
}

internal fun ObjectMapperFactoryBuilder.configureTime() {
  addModule(TimeModule())

  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
  configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
}
