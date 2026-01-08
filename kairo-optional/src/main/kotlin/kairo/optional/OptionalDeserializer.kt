package kairo.optional

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

internal class OptionalDeserializer : StdDeserializer<Optional<*>>(
  Optional::class.java,
) {
  override fun getNullValue(ctxt: DeserializationContext?): Optional<*>? {
    return super.getNullValue(ctxt)
  }

  override fun getNullValue(): Optional<*>? {
    return super.getNullValue()
  }

  override fun getAbsentValue(ctxt: DeserializationContext?): Optional.Missing =
    Optional.Missing

  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Optional<*> {
    return Optional.Value(p.readValueAs())
  }
}
