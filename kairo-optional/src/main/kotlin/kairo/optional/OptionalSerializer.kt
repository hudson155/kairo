package kairo.optional

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

internal class OptionalSerializer : StdSerializer<Optional<*>>(
  Optional::class.java,
) {
  override fun isEmpty(provider: SerializerProvider, value: Optional<*>?): Boolean =
    super.isEmpty(provider, value) || value is Optional.Missing

  override fun serialize(
    value: Optional<*>,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    when (value) {
      is Optional.Missing -> error("Serializing an Optional must only include non-empty.")
      is Optional.Null -> gen.writeNull()
      is Optional.Value<*> -> provider.defaultSerializeValue(value.value, gen)
    }
  }
}
