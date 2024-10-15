package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import kairo.serialization.util.readValue

public class IntDeserializer : PrimitiveDeserializer<Int>(Int::class) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Int =
      fromString(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Int =
    p.intValue

  public companion object {
    private fun fromString(string: String): Int =
      string.toInt()
  }
}
