package kairo.serialization.module.money

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kairo.serialization.typeReference
import org.javamoney.moneta.Money

@Suppress("RedundantNullableReturnType")
public class MoneyDeserializer(
  private val moneyFormatter: MoneyFormatter<*>,
) : StdDeserializer<Money>(Money::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  @Suppress("ForbiddenMethodCall")
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Money? =
    moneyFormatter.parse(p.readValueAs(moneyFormatter.type.typeReference))
}
