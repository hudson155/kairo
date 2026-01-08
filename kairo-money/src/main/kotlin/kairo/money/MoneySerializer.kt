package kairo.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.javamoney.moneta.Money

public abstract class MoneySerializer : StdSerializer<Money>(
  Money::class.java,
) {
  public class AsObject : MoneySerializer() {
    override fun serialize(
      value: Money,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      val delegate = MoneyAsObjectDelegate(
        amount = value.number.numberValueExact(),
        currency = value.currency,
      )
      provider.defaultSerializeValue(delegate, gen)
    }
  }
}
