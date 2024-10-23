package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class XmlIntDeserializer : IntDeserializer() {
  public open class Key : IntDeserializer.Key()

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun extract(p: JsonParser): Int {
    val string = p.text
    return convert(string)
  }
}
