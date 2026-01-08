package kairo.optional

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

internal class RequiredSerializer : StdSerializer<Required<*>>(
  Required::class.java,
) {
  override fun isEmpty(provider: SerializerProvider, value: Required<*>?): Boolean =
    super.isEmpty(provider, value) || value is Required.Missing

  override fun serialize(
    value: Required<*>,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    when (value) {
      is Required.Missing -> error("Serializing an Required must only include non-empty.")
      is Required.Value<*> -> provider.defaultSerializeValue(value.value, gen)
    }
  }
}
