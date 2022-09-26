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
