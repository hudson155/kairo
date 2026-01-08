package kairo.money

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.javamoney.moneta.Money

public abstract class MoneyDeserializer : StdDeserializer<Money>(
  Money::class.java,
) {
  public class AsObject : MoneyDeserializer() {
    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): Money {
      val delegate = ctxt.readValue(p, MoneyAsObjectDelegate::class.java)
      return Money.of(delegate.amount, delegate.currency)
    }
  }
}
