package kairo.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.math.BigDecimal
import javax.money.CurrencyUnit
import org.javamoney.moneta.Money

public abstract class MoneyFormat {
  public abstract val serializer: JsonSerializer<Money>
  public abstract val deserializer: JsonDeserializer<Money>

  public object Default : MoneyFormat() {
    private data class Delegate(
      val amount: BigDecimal,
      val currency: CurrencyUnit,
    )

    override val serializer: JsonSerializer<Money> =
      object : StdSerializer<Money>(Money::class.java) {
        override fun serialize(
          value: Money,
          gen: JsonGenerator,
          provider: SerializerProvider,
        ) {
          val delegate = Delegate(
            amount = value.number.numberValueExact(),
            currency = value.currency,
          )
          provider.defaultSerializeValue(delegate, gen)
        }
      }

    override val deserializer: JsonDeserializer<Money> =
      object : StdDeserializer<Money>(Money::class.java) {
        override fun deserialize(
          p: JsonParser,
          ctxt: DeserializationContext,
        ): Money {
          val delegate = ctxt.readValue(p, Delegate::class.java)
          return Money.of(delegate.amount, delegate.currency)
        }
      }
  }
}
