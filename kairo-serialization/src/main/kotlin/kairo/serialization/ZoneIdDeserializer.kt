package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This deserializer intentionally only supports [ZoneRegion] and [ZoneOffset.UTC].
 * Non-UTC [ZoneOffset]s are not supported.
 */
public class ZoneIdDeserializer : StdDeserializer<ZoneId>(ZoneId::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ZoneId? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return convert(string)
  }

  private fun convert(string: String): ZoneId {
    val timeZone = ZoneId.of(string)
    if (timeZone is ZoneOffset) {
      if (timeZone == ZoneOffset.UTC) return timeZone
      throw IllegalArgumentException("The only supported ZoneOffset is UTC.")
    }
    if (timeZone == ZoneId.of("UTC")) {
      return ZoneOffset.UTC
    }
    return timeZone
  }
}
