package kairo.optional

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public class RequiredSerializer<T : Any>(
  private val valueSerializer: KSerializer<T>,
) : KSerializer<Required<T>> {
  override val descriptor: SerialDescriptor = valueSerializer.descriptor

  override fun deserialize(decoder: Decoder): Required<T> =
    Required.Value(decoder.decodeSerializableValue(valueSerializer))

  override fun serialize(encoder: Encoder, value: Required<T>) {
    when (value) {
      is Required.Value -> encoder.encodeSerializableValue(valueSerializer, value.value)
      Required.Missing -> error(
        "Tried to encode missing required value." +
          " Ensure that requireds are annotated with" +
          " @${EncodeDefault::class.simpleName}(${EncodeDefault::class.simpleName}" +
          ".${EncodeDefault.Mode::class.simpleName}" +
          ".${EncodeDefault.Mode.NEVER}).",
      )
    }
  }
}
