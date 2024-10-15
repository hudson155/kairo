package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

public class LongDeserializer : PrimitiveDeserializer<Long>(Long::class) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Long =
      convert(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Long =
    p.longValue

  public companion object {
    private fun convert(string: String): Long =
      string.toLong()
  }
}
