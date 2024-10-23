package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import kairo.serialization.util.getAnnotation

public class StringDeserializer(
  private val trimWhitespace: TrimWhitespace.Type,
) : PrimitiveDeserializer<String>(String::class), ContextualDeserializer {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): String =
      convert(key, trimWhitespace = TrimWhitespace.Type.TrimNone)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_STRING)

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): StringDeserializer =
    StringDeserializer(
      trimWhitespace = property?.getAnnotation<TrimWhitespace>()?.type ?: trimWhitespace,
    )

  override fun extract(p: JsonParser): String {
    val string = p.text
    return convert(string, trimWhitespace = trimWhitespace)
  }

  public companion object {
    @JvmStatic
    private fun convert(string: String, trimWhitespace: TrimWhitespace.Type): String {
      var result = string
      result = trimWhitespace.transform(result)
      return result
    }
  }
}
