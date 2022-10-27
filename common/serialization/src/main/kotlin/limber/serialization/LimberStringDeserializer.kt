package limber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StringDeserializer

internal class LimberStringDeserializer(
  private val trim: TrimWhitespace.Type,
) : StringDeserializer(), ContextualDeserializer {
  /**
   * Creates an instance of this deserializer configured with the proper [trim] value
   * if the property has a [TrimWhitespace] annotation.
   * Otherwise, the default is used.
   */
  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
    val trim = property?.let { it.getAnnotation(TrimWhitespace::class.java)?.type } ?: return this
    return LimberStringDeserializer(trim)
  }

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
    var result = super.deserialize(p, ctxt)
    with(trim) {
      if (trimStart) result = result.trimStart()
      if (trimEnd) result = result.trimEnd()
    }
    return result
  }
}
