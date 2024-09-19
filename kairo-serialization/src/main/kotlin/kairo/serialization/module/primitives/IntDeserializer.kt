package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public class IntDeserializer : PrimitiveDeserializer<Int>(Int::class) {
  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Int =
    p.intValue
}
