package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

public class BooleanDeserializer : PrimitiveDeserializer<Boolean>(Boolean::class) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Boolean =
      fromString(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_TRUE, JsonToken.VALUE_FALSE)

  override fun extract(p: JsonParser): Boolean =
    p.booleanValue

  public companion object {
    private fun fromString(string: String): Boolean =
      string.toBooleanStrict()
  }
}
