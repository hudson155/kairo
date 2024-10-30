package kairo.serialization.module.money

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.javamoney.moneta.Money

@Suppress("RedundantNullableReturnType")
public class MoneyDeserializer(
  private val moneyFormatter: MoneyFormatter<*>,
) : StdDeserializer<Money>(Money::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Money? {
    @Suppress("ForbiddenMethodCall")
    val value = p.readValueAs(moneyFormatter.kClass.java)
    return moneyFormatter.parse(value)
  }
}
