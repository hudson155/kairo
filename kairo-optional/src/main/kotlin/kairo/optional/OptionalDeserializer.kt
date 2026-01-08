package kairo.optional

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

internal class OptionalDeserializer(
  private val valueDeserializer: JsonDeserializer<Any>?,
) : StdDeserializer<Optional<*>>(Optional::class.java), ContextualDeserializer {
  constructor() : this(null)

  override fun createContextual(
    ctxt: DeserializationContext,
    property: BeanProperty?,
  ): JsonDeserializer<*> {
    property ?: return this
    val referencedType = property.type.referencedType ?: return this
    val valueDeserializer = ctxt.findContextualValueDeserializer(referencedType, property)
    return OptionalDeserializer(valueDeserializer)
  }

  override fun getNullValue(ctxt: DeserializationContext?): Optional<*> =
    Optional.Null

  override fun getAbsentValue(ctxt: DeserializationContext?): Optional.Missing =
    Optional.Missing

  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Optional<*> {
    checkNotNull(valueDeserializer) { "Cannot deserialize Optional of unknown type." }
    val value = valueDeserializer.deserialize(p, ctxt)
    return Optional.Value(value)
  }
}
