package kairo.money

import java.math.BigDecimal
import kairo.serialization.BigDecimalSerializer
import kairo.serialization.decodeSerializableValue
import kairo.serialization.encodeSerializableValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.javamoney.moneta.Money

public abstract class MoneySerializer : KSerializer<Money> {
  public class Default : KSerializer<Money> {
    @Serializable
    private data class Delegate(
      @Serializable(with = BigDecimalSerializer.AsString::class)
      val amount: BigDecimal,
      val currency: String,
    )

    override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor(Money::class.qualifiedName!!) {
        element<String>("amount")
        element<String>("currency")
      }

    override fun serialize(encoder: Encoder, value: Money) {
      val delegate = Delegate(
        amount = value.number.numberValueExact<BigDecimal>(),
        currency = value.currency.currencyCode,
      )
      encoder.encodeSerializableValue(delegate)
    }

    override fun deserialize(decoder: Decoder): Money {
      val delegate = decoder.decodeSerializableValue<Delegate>()
      return Money.of(delegate.amount, delegate.currency)
    }
  }
}
