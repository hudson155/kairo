package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class XmlBooleanDeserializer : BooleanDeserializer() {
  public open class Key : BooleanDeserializer.Key()

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun extract(p: JsonParser): Boolean {
    val string = p.text
    return convert(string)
  }
}
