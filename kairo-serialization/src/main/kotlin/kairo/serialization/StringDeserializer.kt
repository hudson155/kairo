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

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): StringDeserializer =
    StringDeserializer(
      trimWhitespace = property?.getAnnotation<TrimWhitespace>()?.type ?: trimWhitespace,
    )

  override fun extract(p: JsonParser): String {
    var result = p.text
    result = trimWhitespace.transform(result)
    return result
  }
}
