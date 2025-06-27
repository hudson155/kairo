@file:Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")

package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This serializer intentionally only supports [ZoneRegion] and [ZoneOffset.UTC].
 * Non-UTC [ZoneOffset]s are not supported.
 */
public class ZoneIdSerializer : StdSerializer<ZoneId>(ZoneId::class.java) {
  public class Key : StdSerializer<ZoneId>(ZoneId::class.java) {
    override fun serialize(value: ZoneId, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: ZoneId, gen: JsonGenerator, provider: SerializerProvider) {
    val string = convert(value)
    gen.writeString(string)
  }

  public companion object {
    @JvmStatic
    private fun convert(value: ZoneId): String {
      if (value is ZoneOffset) {
        if (value == ZoneOffset.UTC) return "UTC"
        throw IllegalArgumentException("The only supported ZoneOffset is UTC.")
      }
      require(value != ZoneId.of("UTC")) { "Prefer ZoneOffset.UTC to ZoneId.of(\"UTC\")." }
      return value.id
    }
  }
}
