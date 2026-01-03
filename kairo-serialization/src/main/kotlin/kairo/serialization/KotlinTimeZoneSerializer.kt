package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId

internal class KotlinTimeZoneSerializer : StdSerializer<TimeZone>(
  TimeZone::class.java
) {
  override fun serialize(
    value: TimeZone,
    gen: JsonGenerator,
    provider: SerializerProvider
  ) {
    val javaZoneId = value.toJavaZoneId()
    provider.defaultSerializeValue(javaZoneId, gen)
  }
}
