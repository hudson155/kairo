package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class IntDeserializer : PrimitiveDeserializer<Int>(Int::class) {
  override val tokens: Set<JsonToken> = setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Int = p.intValue
}
