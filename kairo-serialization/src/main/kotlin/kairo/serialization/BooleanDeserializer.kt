package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class BooleanDeserializer : PrimitiveDeserializer<Boolean>(Boolean::class) {
  override val tokens: Set<JsonToken> = setOf(JsonToken.VALUE_TRUE, JsonToken.VALUE_FALSE)

  override fun extract(p: JsonParser): Boolean = p.booleanValue
}
