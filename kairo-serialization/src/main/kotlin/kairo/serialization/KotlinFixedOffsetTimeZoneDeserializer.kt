package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.toKotlinFixedOffsetTimeZone

internal class KotlinFixedOffsetTimeZoneDeserializer : StdDeserializer<FixedOffsetTimeZone>(
  FixedOffsetTimeZone::class.java
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): FixedOffsetTimeZone {
    val javaZoneOffset = ctxt.readValue(p, java.time.ZoneOffset::class.java)
    return javaZoneOffset.toKotlinFixedOffsetTimeZone()
  }
}
