package kairo.serialization.module.money

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.math.BigDecimal
import kairo.serialization.util.readValue
import org.javamoney.moneta.Money

@Suppress("RedundantNullableReturnType")
public class MoneyDeserializer : StdDeserializer<Money>(Money::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Money? {
    val intermediary = p.readValue<MoneyIntermediary>()
    return Money.of(BigDecimal(intermediary.amount), intermediary.currency)
  }
}
