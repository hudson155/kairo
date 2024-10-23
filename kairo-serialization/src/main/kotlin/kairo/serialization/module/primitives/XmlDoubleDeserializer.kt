package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class XmlDoubleDeserializer : DoubleDeserializer() {
  public open class Key : DoubleDeserializer.Key()

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun extract(p: JsonParser): Double {
    val string = p.text
    return convert(string)
  }
}
