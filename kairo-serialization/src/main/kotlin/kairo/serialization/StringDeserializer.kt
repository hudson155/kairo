package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.ContextualDeserializer

public class StringDeserializer private constructor(
  private val trimWhitespace: TrimWhitespace.Type,
  private val transformCase: TransformCase.Type,
) : PrimitiveDeserializer<String>(String::class), ContextualDeserializer {
  public constructor(
    trimWhitespace: TrimWhitespace.Type,
  ) : this(
    trimWhitespace = trimWhitespace,
    transformCase = TransformCase.Type.None,
  )

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): StringDeserializer =
    StringDeserializer(
      trimWhitespace = property?.getAnnotation<TrimWhitespace>()?.type ?: trimWhitespace,
      transformCase = property?.getAnnotation<TransformCase>()?.type ?: transformCase,
    )

  override fun extract(p: JsonParser): String {
    var result = p.text
    result = trimWhitespace.transform(result)
    result = transformCase.transform(result)
    return result
  }
}
