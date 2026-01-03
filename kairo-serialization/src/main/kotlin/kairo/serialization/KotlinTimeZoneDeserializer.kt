package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinTimeZone

internal class KotlinTimeZoneDeserializer : StdDeserializer<TimeZone>(
  TimeZone::class.java
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): TimeZone {
    val javaZoneId = ctxt.readValue(p, java.time.ZoneId::class.java)
    return javaZoneId.toKotlinTimeZone()
  }
}
