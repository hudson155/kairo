package kairo.serialization

import java.math.BigInteger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializing [BigInteger] as [String] is safer than serializing it as [Long].
 * Serializing to [Long] is error-prone if the value exceeds the available range.
 */
public abstract class BigIntegerSerializer(
  kind: PrimitiveKind,
) : KSerializer<BigInteger> {
  final override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("BigInteger", kind)

  public object AsLong : BigIntegerSerializer(PrimitiveKind.LONG) {
    override fun serialize(encoder: Encoder, value: BigInteger) {
      encoder.encodeLong(value.toLong())
    }

    override fun deserialize(decoder: Decoder): BigInteger =
      BigInteger.valueOf(decoder.decodeLong())
  }

  public object AsString : BigIntegerSerializer(PrimitiveKind.STRING) {
    override fun serialize(encoder: Encoder, value: BigInteger) {
      encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): BigInteger =
      BigInteger(decoder.decodeString())
  }
}
