package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

public open class XmlLongDeserializer : LongDeserializer() {
  public open class Key : LongDeserializer.Key()

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun extract(p: JsonParser): Long {
    val string = p.text
    return convert(string)
  }
}
