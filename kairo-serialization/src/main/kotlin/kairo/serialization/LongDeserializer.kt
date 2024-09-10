package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public class LongDeserializer : PrimitiveDeserializer<Long>(Long::class) {
  override val tokens: Set<JsonToken> = setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Long = p.longValue
}
