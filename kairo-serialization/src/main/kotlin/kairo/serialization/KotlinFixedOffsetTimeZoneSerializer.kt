package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.toJavaZoneOffset

internal class KotlinFixedOffsetTimeZoneSerializer : StdSerializer<FixedOffsetTimeZone>(
  FixedOffsetTimeZone::class.java,
) {
  override fun serialize(
    value: FixedOffsetTimeZone,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaZoneOffset = value.toJavaZoneOffset()
    provider.defaultSerializeValue(javaZoneOffset, gen)
  }
}
