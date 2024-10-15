package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

public class DoubleDeserializer : PrimitiveDeserializer<Double>(Double::class) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Double =
      fromString(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_FLOAT, JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Double =
    p.doubleValue

  public companion object {
    private fun fromString(string: String): Double =
      string.toDouble()
  }
}
