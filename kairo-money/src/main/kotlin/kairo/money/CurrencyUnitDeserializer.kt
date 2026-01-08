package kairo.money

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import javax.money.CurrencyUnit
import javax.money.Monetary

public class CurrencyUnitDeserializer : StdDeserializer<CurrencyUnit>(
  CurrencyUnit::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): CurrencyUnit {
    val string = ctxt.readValue(p, String::class.java)
    return Monetary.getCurrency(string)
  }
}
