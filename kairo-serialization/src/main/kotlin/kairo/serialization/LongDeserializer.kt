package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class LongDeserializer : PrimitiveDeserializer<Long>(Long::class) {
  override val tokens: Set<JsonToken> = setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Long = p.longValue
}
