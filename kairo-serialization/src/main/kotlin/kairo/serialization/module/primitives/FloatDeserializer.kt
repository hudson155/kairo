package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

public class FloatDeserializer : PrimitiveDeserializer<Float>(Float::class) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Float =
      convert(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_FLOAT, JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Float =
    p.floatValue

  public companion object {
    private fun convert(string: String): Float =
      string.toFloat()
  }
}
