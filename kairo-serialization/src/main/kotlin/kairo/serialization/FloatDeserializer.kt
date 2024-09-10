package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class FloatDeserializer : PrimitiveDeserializer<Float>(Float::class) {
  override val tokens: Set<JsonToken> = setOf(JsonToken.VALUE_NUMBER_FLOAT, JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Float = p.floatValue
}
