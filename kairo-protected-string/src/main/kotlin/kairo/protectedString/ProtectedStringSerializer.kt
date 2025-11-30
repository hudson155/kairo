package kairo.protectedString

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ProtectedString.Access::class)
internal object ProtectedStringSerializer : KSerializer<ProtectedString> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor(ProtectedString::class.qualifiedName!!, PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: ProtectedString) {
    encoder.encodeString(value.value)
  }

  override fun deserialize(decoder: Decoder): ProtectedString {
    val value = decoder.decodeString()
    return ProtectedString(value)
  }
}
