package kairo.optional

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

internal class RequiredDeserializer(
  private val valueDeserializer: JsonDeserializer<Any>?,
) : StdDeserializer<Required<*>>(Required::class.java), ContextualDeserializer {
  constructor() : this(null)

  override fun createContextual(
    ctxt: DeserializationContext,
    property: BeanProperty?,
  ): JsonDeserializer<*> {
    property ?: return this
    val referencedType = property.type.referencedType ?: return this
    val valueDeserializer = ctxt.findContextualValueDeserializer(referencedType, property)
    return RequiredDeserializer(valueDeserializer)
  }

  override fun getAbsentValue(ctxt: DeserializationContext?): Required.Missing =
    Required.Missing

  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Required<*> {
    checkNotNull(valueDeserializer) { "Cannot deserialize Required of unknown type." }
    val value = valueDeserializer.deserialize(p, ctxt)
    return Required.Value(value)
  }
}
