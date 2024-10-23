package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class XmlFloatDeserializer : FloatDeserializer() {
  public open class Key : FloatDeserializer.Key()

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun extract(p: JsonParser): Float {
    val string = p.text
    return convert(string)
  }
}
