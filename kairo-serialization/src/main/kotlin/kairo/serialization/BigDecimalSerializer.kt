package kairo.serialization

import java.math.BigDecimal
import java.math.BigInteger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializing [BigInteger] as [String] is safer than serializing it as [Double].
 * Serializing to [Double] is error-prone if the value exceeds the available precision.
 */
public abstract class BigDecimalSerializer(
  kind: PrimitiveKind,
) : KSerializer<BigDecimal> {
  final override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("BigDecimal", kind)

  public object AsDouble : BigDecimalSerializer(PrimitiveKind.DOUBLE) {
    override fun serialize(encoder: Encoder, value: BigDecimal) {
      encoder.encodeDouble(value.toDouble())
    }

    override fun deserialize(decoder: Decoder): BigDecimal =
      BigDecimal.valueOf(decoder.decodeDouble())
  }

  public object AsString : BigDecimalSerializer(PrimitiveKind.STRING) {
    override fun serialize(encoder: Encoder, value: BigDecimal) {
      encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal =
      BigDecimal(decoder.decodeString())
  }
}
