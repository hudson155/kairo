package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.ZoneId
import java.time.ZoneOffset
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This deserializer intentionally only supports [ZoneRegion] and [ZoneOffset.UTC].
 * Non-UTC [ZoneOffset]s are not supported.
 */
@Suppress("RedundantNullableReturnType")
public class ZoneIdDeserializer : StdDeserializer<ZoneId>(ZoneId::class.java) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): ZoneId =
      convert(key)
  }

  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ZoneId? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return convert(string)
  }

  public companion object {
    @JvmStatic
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
}
