package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime

internal class KotlinLocalTimeSerializer : StdSerializer<LocalTime>(
  LocalTime::class.java,
) {
  override fun serialize(
    value: LocalTime,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaLocalTime = value.toJavaLocalTime()
    provider.defaultSerializeValue(javaLocalTime, gen)
  }
}
