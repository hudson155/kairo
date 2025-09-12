package kairo.optional

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public class OptionalSerializer<T>(
  private val valueSerializer: KSerializer<T>,
) : KSerializer<Optional<T>> {
  override val descriptor: SerialDescriptor = valueSerializer.descriptor

  override fun deserialize(decoder: Decoder): Optional<T> {
    if (decoder.decodeNotNullMark()) {
      return Optional.Value(decoder.decodeSerializableValue(valueSerializer))
    }
    decoder.decodeNull()
    return Optional.Null
  }

  override fun serialize(encoder: Encoder, value: Optional<T>) {
    when (value) {
      is Optional.Value -> encoder.encodeSerializableValue(valueSerializer, value.value)
      Optional.Null -> encoder.encodeNull()
      Optional.Missing -> error("Implementation error.")
    }
  }
}
