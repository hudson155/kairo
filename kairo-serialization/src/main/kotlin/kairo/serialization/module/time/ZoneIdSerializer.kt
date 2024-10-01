package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZoneRegion

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This serializer intentionally only supports [ZoneRegion] and [ZoneOffset.UTC].
 * Non-UTC [ZoneOffset]s are not supported.
 */
public class ZoneIdSerializer : StdSerializer<ZoneId>(ZoneId::class.java) {
  override fun serialize(value: ZoneId, gen: JsonGenerator, provider: SerializerProvider) {
    val string = convert(value)
    gen.writeString(string)
  }

  private fun convert(timeZone: ZoneId): String {
    if (timeZone is ZoneOffset) {
      if (timeZone == ZoneOffset.UTC) return "UTC"
      throw IllegalArgumentException("The only supported ZoneOffset is UTC.")
    }
    require(timeZone != ZoneId.of("UTC")) { "Prefer ZoneOffset.UTC to ZoneId.of(\"UTC\")." }
    return timeZone.id
  }
}
