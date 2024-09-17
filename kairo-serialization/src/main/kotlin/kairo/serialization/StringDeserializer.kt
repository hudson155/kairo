package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.ContextualDeserializer

public class StringDeserializer(
  private val trimWhitespace: TrimWhitespace.Type,
) : PrimitiveDeserializer<String>(String::class), ContextualDeserializer {
  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): StringDeserializer {
    val trim = property?.let { it.getAnnotation(TrimWhitespace::class.java)?.type } ?: return this
    return StringDeserializer(trim)
  }

  override fun extract(p: JsonParser): String {
    var result = p.text
    with(trimWhitespace) {
      if (trimStart) result = result.trimStart()
      if (trimEnd) result = result.trimEnd()
    }
    return result
  }
}
